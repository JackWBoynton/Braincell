package bottomtextdanny.braincell.mod.rendering.modeling.chest;

import bottomtextdanny.braincell.mod._base.rendering.core_modeling.BCJoint;

public class BCBaseSingleChestModel extends BCChestModel {

    public BCBaseSingleChestModel() {
        this.texWidth = 64;
        this.texHeight = 64;

        this.chest = new BCJoint(this);
        this.chest.setPosCore(0.0F, 0.0F, 0.0F);
        this.chest.uvOffset(0, 19).addBox(-7.0F, -10.0F, -7.0F, 14.0F, 10.0F, 14.0F, 0.0F, false);

        this.lid = new BCJoint(this);
        this.lid.setPosCore(0.0F, -9.5F, 6.5F);
        this.chest.addChild(this.lid);
        this.lid.uvOffset(0, 0).addBox(-7.0F, -4.5F, -13.5F, 14.0F, 5.0F, 14.0F, 0.0F, false);
        this.lid.uvOffset(0, 0).addBox(-1.0F, -1.5F, -14.5F, 2.0F, 4.0F, 1.0F, 0.0F, false);
    }
}
