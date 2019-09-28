package com.ritualsoftheold.foreman;

import com.ritualsoftheold.terra.core.chunk.ChunkLArray;
import com.ritualsoftheold.terra.core.materials.Registry;
import com.ritualsoftheold.terra.core.materials.TerraModule;
import com.ritualsoftheold.terra.core.materials.TerraObject;
import com.ritualsoftheold.terra.server.chunks.ChunkGenerator;
import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import com.ritualsoftheold.weltschmerz.misc.utils.Random;

import java.nio.ByteBuffer;

public class Foreman implements ChunkGenerator {
    private int treeDistanceX;
    private int treeDistanceY;
    private int treeDistanceZ;
    private byte treeID;

    private ByteBuffer dirtID;
    private ByteBuffer airID;
    private ByteBuffer grassID;
    private ByteBuffer grassMeshID;
    private Weltschmerz weltschmerz;
    private Random random;

    private Registry reg;

    Foreman() {
        weltschmerz = new Weltschmerz();
        random = weltschmerz.getRandom();
    }

    @Override
    public void setMaterials(TerraModule mod, Registry reg) {
        airID = ByteBuffer.allocate(4).putInt(reg.getMaterial("base:air").getWorldId());
        dirtID = ByteBuffer.allocate(4).putInt(reg.getMaterial(mod, "dirt").getWorldId());
        grassID = ByteBuffer.allocate(4).putInt(reg.getMaterial(mod, "grass").getWorldId());
        grassMeshID = ByteBuffer.allocate(4).putInt(reg.getMaterial(mod, "Tall_Grass-mesh_variant01-01").getWorldId());

        TerraObject tree = reg.getMaterial(mod, "birch-02_baked");
        treeDistanceX = tree.getMesh().getDefaultDistanceX();
        treeDistanceY = tree.getMesh().getDefaultDistanceY();
        treeDistanceZ = tree.getMesh().getDefaultDistanceZ();
        treeID = tree.getMesh().getId();
        this.reg = reg;
    }

    @Override
    public ChunkLArray getChunk(float posX, float posY, float posZ) {
        ChunkLArray chunk = new ChunkLArray(posX, posY, posZ, airID, reg);

        int posx = (int) (posX / 16);
        int posz = (int) (posZ / 16);
        int posy = (int) (posY * 4);

        boolean isDifferent = false;

        for (int z = 0; z < 64; z++) {
            for (int x = 0; x < 64; x++) {
                int elevation = (int) Math.round(weltschmerz.getElevation(x + posx * 64, z + posz * 64));
                for (int y = 0; y < 64; y++) {
                    if ((elevation / 64) > (posy / 64)) {
                        chunk.set(x, y, z, dirtID);
                    } else if (elevation / 64 == (posy / 64)) {
                        if (Math.abs(elevation % 64) >= y) {
                            chunk.set(x, y, z, dirtID);
                            isDifferent = true;
                        }
                    }
                }
                if (isDifferent) {
                    chunk.set(x, Math.abs(elevation % 64), z, grassID);
                    if (random.getBoolean()) {
                        chunk.set(x, Math.abs(elevation % 64) + 1, z, grassMeshID);
                    }
                }
            }
        }

        /*
        if (posX >= 5 && posY / 64 >= 0 && posZ >= 5 && treeDistanceX > 0 && treeDistanceY > 0 && treeDistanceZ > 0) {

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

           /* int additionY = 0;
            for (int z = 0; z < sizeZ; z++) {
                for (int x = 0; x < sizeX; x++) {
                    int elevation = (int) Math.round(weltschmerz.getElevation(x + posX * 64, z + posZ * 64));
                    if (posY / 64 == elevation / 64) {
                        if (elevation % 64 > additionY) {
                            additionY = elevation % 64;
                        }
                        for (int y = elevation % 64; y < 64; y++) {
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
                    */

        chunk.setDifferent(isDifferent);

        return chunk;
    }
}
