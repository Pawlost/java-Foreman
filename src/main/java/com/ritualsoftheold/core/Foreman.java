package com.ritualsoftheold.core;

import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import com.ritualsoftheold.weltschmerz.misc.misc.Random;
import xerial.larray.LByteArray;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Foreman {
    private int treeDistanceX;
    private int treeDistanceY;
    private int treeDistanceZ;
    private byte treeID;

    private int dirtID;
    private int grassID;
    private int grassMeshID;
    private boolean isDifferent;
    private Weltschmerz weltschmerz;
    private Random random;

    public Foreman(){
        weltschmerz = new Weltschmerz();
        random = weltschmerz.getRandom();
    }

    public void setMaterials(int dirtID, int grassID, int grassMeshID) {
        this.dirtID = dirtID;
        this.grassID = grassID;
        this.grassMeshID = grassMeshID;
    }

    public void setObject(int treeDistanceX, int treeDistanceY, int treeDistanceZ, byte value) {
        this.treeDistanceX = treeDistanceX;
        this.treeDistanceY = treeDistanceY;
        this.treeDistanceZ = treeDistanceZ;

        this.treeID = value;
    }

   public   boolean isDifferent() {
        return isDifferent;
    }


    public LByteArray getChunk(int posX, int posY, int posZ, LByteArray chunk) {
        posX = posX/16;
        posZ = posZ/16;
        posY = posY * 4;

        int bufferSize = (int) chunk.size();

        ByteBuffer blockBuffer = ByteBuffer.allocate(bufferSize);
        byte[] fill = new byte[bufferSize];
        Arrays.fill(fill, (byte) 1);
        blockBuffer.put(fill);

        isDifferent = false;

        for (int z = 0; z < 64; z++) {
            for (int x = 0; x < 64; x++) {
                int elevation = (int) Math.round(weltschmerz.getElevation(x + posX * 64, z + posZ * 64));
                for (int y = 0; y < 64; y++) {
                    if ((elevation / 64) > (posY / 64)) {
                        blockBuffer.put(x + (y * 64) + (z * 4096), (byte) dirtID);
                    } else if (elevation / 64 == (posY / 64)) {
                        if (Math.abs(elevation % 64) >= y) {
                            blockBuffer.put(x + (y * 64) + (z * 4096), (byte) dirtID);
                            isDifferent = true;
                        }
                    }
                }
                if (isDifferent) {
                    blockBuffer.put(x + Math.abs((elevation % 64) * 64) + (z * 4096), (byte) grassID);
                    if(random.getBoolean()) {
                        blockBuffer.put(x + Math.abs(((elevation + 1)% 64) * 64) + (z * 4096), (byte) grassMeshID);
                    }
                }
            }
        }

        if(posX >= 5 && posY/64 >= 0 && posZ >= 5 && treeDistanceX > 0 && treeDistanceY > 0 && treeDistanceZ > 0) {

            int sizeX;
            boolean accross = false;
            if (treeDistanceX / 64 > 0) {
                sizeX = 64;
                treeDistanceX -= 64;
                accross = true;
            } else {
                sizeX = treeDistanceX % 64;
            }

            int sizeY;
            if (treeDistanceY / 64 > 0) {
                sizeY = 64;
                treeDistanceY -= 64;
                accross = true;
            } else {
                sizeY = treeDistanceY % 64;
            }

            int sizeZ;
            if (treeDistanceZ / 64 > 0) {
                sizeZ = 64;
                treeDistanceZ -= 64;
                accross = true;
            } else {
                sizeZ = treeDistanceZ % 64;
            }

            if (!accross) {
                treeDistanceX -= treeDistanceX % 64;
                treeDistanceY -= treeDistanceY % 64;
                treeDistanceZ -= treeDistanceZ % 64;
            }

            int additionY = 0;
            for (int z = 0; z < sizeZ; z++) {
                for (int x = 0; x < sizeX; x++) {
                    int elevation = (int) Math.round(weltschmerz.getElevation(x + posX * 64, z + posZ * 64));
                    if (posY / 64 == elevation / 64) {
                        elevation += 1;
                        if(elevation%64 > additionY){
                            additionY = elevation%64;
                        }
                        for (int y = elevation % 63; y < 64; y++) {
                            blockBuffer.put(x + (y * 64) + (z * 4096), treeID);
                        }
                    } else {
                        for (int y = 0; y < sizeY; y++) {
                            blockBuffer.put(x + (y * 64) + (z * 4096), treeID);
                        }
                    }
                }
            }
            treeDistanceY += additionY;
            isDifferent = true;
        }

        blockBuffer.rewind();
        chunk.write(blockBuffer);
        return chunk;
    }
}
