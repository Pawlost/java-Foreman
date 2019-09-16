package com.ritualsoftheold.foreman.config;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import com.ritualsoftheold.loader.BlockMaker;
import com.ritualsoftheold.loader.ModelLoader3D;
import com.ritualsoftheold.terra.core.materials.TerraMesh;
import com.ritualsoftheold.terra.core.materials.TerraModule;
import com.ritualsoftheold.terra.core.materials.TerraTexture;

public class PrimitiveResourcePack {

    private AssetManager assetManager;

    public PrimitiveResourcePack(AssetManager assetManager){
        this.assetManager = assetManager;
    }

    public void registerObjects(TerraModule mod){

        ModelLoader3D modelLoader3D = new ModelLoader3D(assetManager);

        //Textures
        mod.newMaterial().name("dirt").texture(new TerraTexture("NorthenForestDirt256px.png"));
        mod.newMaterial().name("grass").texture(new TerraTexture("NorthenForestGrass256px.png"));

        Spatial asset =  modelLoader3D.getMesh("Tall_Grass-mesh_variant01-01");

        //Custom meshes
        BlockMaker maker = new BlockMaker(asset);

        mod.newMaterial().name("Tall_Grass-mesh_variant01-01").model(new TerraMesh("Tall_Grass-mesh_variant01-01",
                maker.getDefaultDistanceX(), maker.getDefaultDistanceY(), maker.getDefaultDistanceZ()))
                .texture(new TerraTexture("Tall_grass-texture-02.png", true));

        Spatial spatial = modelLoader3D.getMesh("birch-02_baked");

        maker = new BlockMaker(spatial);

        mod.newMaterial().name("birch-02_baked").model(new TerraMesh("birch-02_baked",
                maker.getDefaultDistanceX(), maker.getDefaultDistanceY(), maker.getDefaultDistanceZ()))
                .texture(new TerraTexture("birch-02_baked.png", true));
    }
}
