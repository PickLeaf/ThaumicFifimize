package com.pickleaf.thaumic_fifimize.common.tile;

import thaumcraft.common.blocks.world.ore.BlockCrystal;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.blocks.TileThaumcraft;
import thaumcraft.common.blocks.IBlockEnabled;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockMist;
import thaumcraft.common.lib.utils.BlockStateUtils;

public class TileLampCrystal extends TileThaumcraft implements IEssentiaTransport, IBlockEnabled, ITickable {

    private boolean reserve = false;
    public int charges = -1;
    int lastX = 0;
    int lastY = 0;
    int lastZ = 0;
    Block lastBlockID;
    int lastMetadata;
    ArrayList<BlockPos> checklist;
    int drawDelay;

    public TileLampCrystal() {
        this.lastBlockID = Blocks.air;
        this.lastMetadata = 0;
        this.checklist = new ArrayList<BlockPos>();
        this.drawDelay = 0;
    }

    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        if (this.worldObj != null && this.worldObj.isRemote) {
            this.worldObj.checkLightFor(EnumSkyBlock.BLOCK, this.getPos());
        }

    }

    public void update() {
        if (this.worldObj == null) {
            return;
        }
        if (!this.worldObj.isRemote) {
            if (this.charges <= 0) {
                if (this.reserve) {
                    this.charges = 100;
                    this.reserve = false;
                    this.markDirty();
                    this.worldObj.markBlockForUpdate(this.getPos());
                } else if (this.drawEssentia()) {
                    this.charges = 100;
                    this.markDirty();
                    this.worldObj.markBlockForUpdate(this.getPos());
                }

                if (this.charges <= 0) {
                    if (BlockStateUtils.isEnabled(this.getBlockMetadata())) {
                        this.worldObj.setBlockState(this.pos,
                                this.worldObj.getBlockState(this.getPos()).withProperty(IBlockEnabled.ENABLED,
                                        false),
                                3);
                    }
                } else if (!this.gettingPower() && !BlockStateUtils.isEnabled(this.getBlockMetadata())) {
                    this.worldObj.setBlockState(this.pos,
                            this.worldObj.getBlockState(this.getPos()).withProperty(IBlockEnabled.ENABLED,
                                    true),
                            3);
                }
            }

            if (!this.reserve && this.drawEssentia()) {
                this.reserve = true;
            }

            if (this.charges == 0) {
                this.charges = -1;
                this.worldObj.markBlockForUpdate(this.getPos());
            }

            if (!this.gettingPower() && this.charges > 0) {
                this.updateCrystal();
            }
        }
    }

    private void updateCrystal() {
        BlockPos lastBlockPos = new BlockPos(this.lastX, this.lastY, this.lastZ);
        IBlockState lastBlockState = this.worldObj.getBlockState(lastBlockPos);
        if (this.lastBlockID != lastBlockState.getBlock()
                || this.lastMetadata != lastBlockState.getBlock().getMetaFromState(lastBlockState)) {
            EntityPlayer p = this.worldObj.getClosestPlayer((double) this.lastX, (double) this.lastY,
                    (double) this.lastZ,
                    (double) 32.0F);
            if (p != null) {
                PacketHandler.INSTANCE.sendToAllAround(
                        new PacketFXBlockMist(lastBlockPos, 8440779),
                        new NetworkRegistry.TargetPoint(this.worldObj.provider.getDimensionId(),
                                (double) this.lastX, (double) this.lastY, (double) this.lastZ, (double) 32.0F));
            }

            this.lastBlockID = lastBlockState.getBlock();
            this.lastMetadata = lastBlockState.getBlock().getMetaFromState(lastBlockState);
        }

        int distance = 1;
        if (this.checklist.size() == 0) {
            for (int a = -distance; a <= distance; ++a) {
                for (int b = -distance; b <= distance; ++b) {
                    this.checklist.add(this.getPos().add(a, distance, b));
                }
            }

            Collections.shuffle(this.checklist, this.worldObj.rand);
        }

        int x = ((BlockPos) this.checklist.get(0)).getX();
        int y = ((BlockPos) this.checklist.get(0)).getY();
        int z = ((BlockPos) this.checklist.get(0)).getZ();
        this.checklist.remove(0);

        while (y >= this.pos.getY() - distance) {
            BlockPos bp = new BlockPos(x, y, z);
            Block crystal = this.worldObj.getBlockState(bp).getBlock();
            if (!this.worldObj.isAirBlock(bp) && crystal instanceof BlockCrystal) {
                this.lastX = x;
                this.lastY = y;
                this.lastZ = z;
                IBlockState bs2 = this.worldObj.getBlockState(bp);
                this.lastBlockID = bs2.getBlock();
                this.lastMetadata = bs2.getBlock().getMetaFromState(bs2);
                crystal.updateTick(this.worldObj, bp, bs2, this.worldObj.rand);
                if (this.worldObj.rand.nextInt(3 + ((BlockCrystal) crystal).getGeneration(bs2)) == 0)
                    --this.charges;
                return;
            }

            --y;
        }

    }

    public void readCustomNBT(NBTTagCompound nbttagcompound) {
        this.reserve = nbttagcompound.getBoolean("reserve");
        this.charges = nbttagcompound.getInteger("charges");
    }

    public void writeCustomNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setBoolean("reserve", this.reserve);
        nbttagcompound.setInteger("charges", this.charges);
    }

    boolean drawEssentia() {
        if (this.worldObj == null) {
            return false;
        }
        if (++this.drawDelay % 5 != 0) {
            return false;
        } else {
            TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.worldObj, this.getPos(),
                    BlockStateUtils.getFacing(this.getBlockMetadata()));
            if (te != null) {
                IEssentiaTransport ic = (IEssentiaTransport) te;
                if (!ic.canOutputTo(BlockStateUtils.getFacing(this.getBlockMetadata()).getOpposite())) {
                    return false;
                }

                if (ic.getSuctionAmount(BlockStateUtils.getFacing(this.getBlockMetadata()).getOpposite()) < this
                        .getSuctionAmount(BlockStateUtils.getFacing(this.getBlockMetadata()))
                        && ic.takeEssentia(Aspect.CRYSTAL, 1,
                                BlockStateUtils.getFacing(this.getBlockMetadata()).getOpposite()) == 1) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean isConnectable(EnumFacing face) {
        if (this.worldObj == null) {
            return false;
        }
        return face == BlockStateUtils.getFacing(this.getBlockMetadata());
    }

    public boolean canInputFrom(EnumFacing face) {
        if (this.worldObj == null) {
            return false;
        }
        return face == BlockStateUtils.getFacing(this.getBlockMetadata());
    }

    public boolean canOutputTo(EnumFacing face) {
        return false;
    }

    public void setSuction(Aspect aspect, int amount) {
    }

    public int getMinimumSuction() {
        return 0;
    }

    public Aspect getSuctionType(EnumFacing face) {
        return Aspect.CRYSTAL;
    }

    public int getSuctionAmount(EnumFacing face) {
        if (this.worldObj == null) {
            return 0;
        }
        return face != BlockStateUtils.getFacing(this.getBlockMetadata()) || this.reserve && this.charges > 0 ? 0 : 128;
    }

    public Aspect getEssentiaType(EnumFacing loc) {
        return null;
    }

    public int getEssentiaAmount(EnumFacing loc) {
        return 0;
    }

    public int takeEssentia(Aspect aspect, int amount, EnumFacing loc) {
        return 0;
    }

    public int addEssentia(Aspect aspect, int amount, EnumFacing loc) {
        return 0;
    }
}
