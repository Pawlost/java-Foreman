package com.ritualsoftheold.foreman;

import com.ritualsoftheold.terra.server.world.ChunkGenerator;

public class CGHandler {
    private Foreman foreman;

    public CGHandler() {
        foreman = new Foreman();
    }

    public ChunkGenerator getGenerator() {
        return foreman;
    }
}
