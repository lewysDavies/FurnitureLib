package de.Ste3et_C0st.FurnitureLib.Utilitis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Bed.Part;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import de.Ste3et_C0st.FurnitureLib.Crafting.Project;
import de.Ste3et_C0st.FurnitureLib.main.FurnitureLib;
import de.Ste3et_C0st.FurnitureLib.main.ObjectID;
import de.Ste3et_C0st.FurnitureLib.main.Type.CenterType;
import de.Ste3et_C0st.FurnitureLib.main.Type.PlaceableSide;
import de.Ste3et_C0st.FurnitureLib.main.Type.SQLAction;

public class LocationUtil {

	public final BlockFace[] axis = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
	public List<BlockFace> axisList = Arrays.asList(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);
    public final BlockFace[] radial = { BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST };
    public short getFromDey(short s){return (short) (15-s);}
    public BlockFace yawToFaceRadial(float yaw) { return radial[Math.round(yaw / 45f) & 0x7];}
    public BlockFace yawToFace(float yaw) {return axis[Math.round(yaw / 90f) & 0x3];}
	
    public BlockFace yawToFace(float yaw, float pitch) {
        if(pitch<-80){
        	return BlockFace.UP;
        }else if(pitch>80){
        	return BlockFace.DOWN;
        }
    	return axis[Math.round(yaw / 90f) & 0x3];
    }
    
    public EulerAngle degresstoRad(EulerAngle degressAngle){
    	return new EulerAngle(degressAngle.getX() * Math.PI / 180, degressAngle.getY() * Math.PI / 180, degressAngle.getZ() * Math.PI / 180);
    }
    
    public EulerAngle Radtodegress(EulerAngle degressAngle){
    	return new EulerAngle(degressAngle.getX() * 180 / Math.PI, degressAngle.getY() * 180 / Math.PI, degressAngle.getZ() * 180 / Math.PI);
    }
    
    public int FaceToYaw(final BlockFace face) {
        switch (face) {
            case NORTH: return 0;
            case NORTH_EAST: return 45;
            case EAST: return 90;
            case SOUTH_EAST: return 135;
            case SOUTH: return 180;
            case SOUTH_WEST: return 225;
            case WEST: return 270;
            case NORTH_WEST: return 315;
            default: return 0;
        }
    }
	
//	@SuppressWarnings("deprecation")
//	public Color getDyeFromDurability(short s){
//		return DyeColor.getByDyeData((byte) s).getColor();
//	}
	
	/* Check if the Furniture have enougth space */
	public boolean canBuild(Location loc, Project pro, Player p){
		BlockFace face = yawToFace(loc.getYaw()).getOppositeFace();
		loc = loc.add(0, 1, 0);
		//if(!FurnitureLib.getInstance().getPermManager().canBuild(p, loc)){return false;}
		int w = pro.getWitdh();
		int h = pro.getHeight();
		int l = pro.getLength();
		CenterType type = pro.getCenterType();
		List<Location> blockList = new ArrayList<Location>();
		Vector v2 = loc.toVector();
		for(ObjectID obj : FurnitureLib.getInstance().getFurnitureManager().getObjectList()){
			if(obj==null) continue;
			if(obj.getStartLocation()==null) continue;
			Vector v1 = obj.getStartLocation().toVector();
			if(v1.equals(v2)){
				if(!obj.getSQLAction().equals(SQLAction.REMOVE)){
					p.sendMessage(FurnitureLib.getInstance().getLangManager().getString("message.FurnitureOnThisPlace"));
					return false;
				}
			}
		}
		switch (type) {
		case RIGHT:
			if(!pro.getPlaceableSide().equals(PlaceableSide.SIDE)){
				for(int a = 0; a<w;a++){
					for(int b = 0; b<h;b++){
						for(int c = 0;c<l;c++){
							Location location = FurnitureLib.getInstance().getLocationUtil().getRelativ(loc, face,(double) -a,(double) c).add(0, b, 0);
							//if(!FurnitureLib.getInstance().getPermManager().canBuild(p, location)){return false;}
							if(!location.getBlock().getType().equals(Material.AIR)){blockList.add(location);}
						}
					}
				}
			}else{
				for(int a = 0; a<w;a++){
					for(int b = 0; b<h;b++){
						for(int c = 0;c>l;c--){
							Location location = FurnitureLib.getInstance().getLocationUtil().getRelativ(loc, face,(double) -a,(double) c).add(0, b, 0);
							//if(!FurnitureLib.getInstance().getPermManager().canBuild(p, location)){return false;}
							if(!location.getBlock().getType().equals(Material.AIR)){blockList.add(location);}
						}
					}
				}
			}
			break;
		case LEFT:
			for(int a = 0; a<w;a++){
				for(int b = 0; b<h;b++){
					for(int c = 0;c<l;c++){
						Location location = FurnitureLib.getInstance().getLocationUtil().getRelativ(loc, face,(double) -a,(double) c).add(0, b, 0);
						//if(!FurnitureLib.getInstance().getPermManager().canBuild(p, location)){return false;}
						if(!location.getBlock().getType().equals(Material.AIR)){blockList.add(location);}
					}
				}
			}
			break;
		case FRONT:
			for(int a = 0; a<w;a++){
				for(int b = 0; b<h;b++){
					for(int c = 0;c<l;c++){
						Location location = FurnitureLib.getInstance().getLocationUtil().getRelativ(loc, face,(double) a,(double) c).add(0, b, 0);
						//if(!FurnitureLib.getInstance().getPermManager().canBuild(p, location)){return false;}
						if(!location.getBlock().getType().equals(Material.AIR)){blockList.add(location);}
					}
				}
			}
			break;
		case CENTER:
			Double d = Math.ceil((double) l/2);
			int w1 = d.intValue();
			for(int a = 0; a<w;a++){
				for(int b = 0; b<h;b++){
					for(int c = 0;c<w1;c++){
						Location location = FurnitureLib.getInstance().getLocationUtil().getRelativ(loc, face,(double) -a,(double) c).add(0, b, 0);
						//if(!FurnitureLib.getInstance().getPermManager().canBuild(p, location)){return false;}
						if(!location.getBlock().getType().equals(Material.AIR)){blockList.add(location);}
					}
				}
			}
			for(int a = 0; a<w;a++){
				for(int b = 0; b<h;b++){
					for(int c = 0;c<w1;c++){
						Location location = FurnitureLib.getInstance().getLocationUtil().getRelativ(loc, face,(double) -a,(double) -c).add(0, b, 0);
						//if(!FurnitureLib.getInstance().getPermManager().canBuild(p, location)){return false;}
						if(!location.getBlock().getType().equals(Material.AIR)){blockList.add(location);}
					}
				}
			}
			break;
		}
		
		
		
		if(blockList.isEmpty()){
			return true;
		}else{
			for(Location location : blockList){
				particleBlock(location.getBlock(), p);
			}	
			p.sendMessage(FurnitureLib.getInstance().getLangManager().getString("message.NotEnoughSpace"));
			return false;
		}
	}
	
	public void particleBlock(Block b, Player p){
		particleBlock(b, p, org.bukkit.Particle.REDSTONE, 1);
	}
	
	public void particleBlock(Block b, Player p, org.bukkit.Particle particleData, float value){
		if(!FurnitureLib.getInstance().isParticleEnable()) return;
		try{
			Location loc = b.getLocation();
			for(double x = .0; x<1d; x+=.3){
				for(double y = .0; y<1d; y+=.3){
					for(double z = .0; z<1d; z+=.3){
						Location location = loc.clone();
						location = location.add(x, y, z);
//						WrapperPlayServerWorldParticles particle = new WrapperPlayServerWorldParticles();
//						particle.setX((float) location.getX());
//						particle.setY((float) location.getY());
//						particle.setZ((float) location.getZ());
//						particle.setOffsetX(.1F);
//						particle.setOffsetY(.1F);
//						particle.setOffsetZ(.1F);
//						particle.setLongDistance(true);
//						particle.setNumberOfParticles(1);
//						particle.setParticleData(value);s
//						particle.setParticleType(particleData);
//						particle.sendPacket(p);
						p.spawnParticle(particleData, location, 1, new Particle.DustOptions(Color.RED, 1));
					}
				}
			}
			
		}catch(Exception e){e.printStackTrace();}
	}

    public boolean isDay(World w) {
        long time = w.getTime();
     
        if(time > 0 && time < 12300) {
            return true;
        } else {
            return false;
        }
    }
    
    public Vector getRelativ(Vector v1, double x, BlockFace bf){
    	switch(bf){
    	case NORTH: v1.add(new Vector(0, 0,x)); break;
    	case EAST: v1.add(new Vector(x, 0, 0)); break;
    	case SOUTH: v1.add(new Vector(0, 0,-x)); break;
    	case WEST: v1.add(new Vector(x, 0, 0)); break;
    	case DOWN:v1.add(new Vector(0, -x, 0));break;
    	case UP:v1.add(new Vector(0, x, 0));break;
		default: v1.add(new Vector(x, 0, 0)); break;
    	}
    	return v1;
    }
    
    public BlockFace StringToFace(final String face) {
        switch (face) {
            case "NORTH": return BlockFace.NORTH;
            case "EAST": return BlockFace.EAST;
            case "SOUTH": return BlockFace.SOUTH;
            case "WEST": return BlockFace.WEST;
            case "UP": return BlockFace.UP;
            case "DOWN": return BlockFace.DOWN;
            case "NORTH_NORTH_EAST": return BlockFace.NORTH_NORTH_EAST;
            case "NORTH_NORTH_WEST": return BlockFace.NORTH_NORTH_WEST;
            case "NORTH_WEST": return BlockFace.NORTH_WEST;
            case "EAST_NORTH_EAST": return BlockFace.EAST_NORTH_EAST;
            case "EAST_SOUTH_EAST": return BlockFace.EAST_SOUTH_EAST;
            case "SOUTH_EAST": return BlockFace.SOUTH_EAST;
            case "SOUTH_SOUTH_EAST": return BlockFace.SOUTH_SOUTH_EAST;
            case "SOUTH_SOUTH_WEST": return BlockFace.SOUTH_SOUTH_WEST;
            case "SOUTH_WEST": return BlockFace.SOUTH_WEST;
            case "WEST_NORTH_WEST": return BlockFace.WEST_NORTH_WEST;
            case "WEST_SOUTH_WEST": return BlockFace.WEST_SOUTH_WEST;
            default: return BlockFace.NORTH;
        }
    }
    
    public Block setSign(BlockFace face, Location l) {
			return setSign(face, l, Material.OAK_SIGN);
    }
    
    @SuppressWarnings("deprecation")
	public Block setSign(BlockFace face, Location l, Material material) {
			l.getBlock().setType(Material.AIR);
			l.getBlock().setType(material);
			Block block = l.getBlock();
			BlockState state = l.getBlock().getState();
			state.setRawData((byte) getFacebyte(yawToFace(FaceToYaw(face.getOppositeFace()) - 90)));
    		state.update(false);
			return block;
    }
    
    public byte getFacebyte(BlockFace b){
    	switch (b) {
		case NORTH:return 0x4;
		case EAST:return 0x2;
		case SOUTH:return 0x5;
		case WEST:return 0x3;
		default:return 0x5;
		}
    }
    
    public Location setBed(BlockFace face, Location l, Material mat) {
		Block block = l.getBlock();
		block.setType(mat);
		if(block.getBlockData() instanceof Directional) {
			Directional bState = (Directional) block.getBlockData();
			bState.setFacing(face.getOppositeFace());
			block.setBlockData(bState, false);
			block = block.getRelative(face.getOppositeFace());
			block.setType(mat, false);
			if(block.getBlockData() instanceof Directional) {
				bState = (Directional) block.getBlockData();
				bState.setFacing(face.getOppositeFace());
				if(block.getBlockData() instanceof Bed) {
					Bed head = (Bed) bState;
					head.setPart(Part.HEAD);
				}
				block.setBlockData(bState, false);
			}
		}
		return l;
    }
    
    public Block setHalfBed(BlockFace face, Location l, Material mat) {
    	Block block = l.getBlock();
		block.setType(mat);
		if(block.getBlockData() instanceof Directional) {
			Directional bState = (Directional) block.getBlockData();
			bState.setFacing(face);
			block.setBlockData(bState, false);
			block.getRelative(face.getOppositeFace()).setType(Material.AIR);
			return block;
		}
		return null;
    }
    
    private Double round(Double d){
    	BigDecimal b = new BigDecimal(d);
    	b = b.setScale(2,BigDecimal.ROUND_HALF_UP);
    	return b.doubleValue();
    }
    
	public Location getRelativ(Location loc, BlockFace b, double z, double x){
		Location l = loc.clone();
		l.setYaw(FaceToYaw(b));
		x = round(x);
		z = round(z);
		switch (b) {
		case NORTH:
			l.add(x,0,z);
			break;
		case SOUTH:
			l.add(-x,0,-z);
			break;
		case WEST:
			l.add(z,0,-x);
			break;
		case EAST:
			l.add(-z,0,x);
			break;
		case NORTH_EAST:
			l.add(x,0,z);
			l.add(-z,0,x);
			break;
		case NORTH_NORTH_EAST:
			l.add(x,0,z);
			l.add(x,0,z);
			l.add(-z,0,x);
			break;
		case NORTH_NORTH_WEST:
			l.add(x,0,z);
			l.add(x,0,z);
			l.add(z,0,-x);
			break;
		case NORTH_WEST:
			l.add(x,0,z);
			l.add(z,0,-x);
			break;
		case EAST_NORTH_EAST:
			l.add(-z,0,x);
			l.add(x,0,z);
			l.add(-z,0,x);
			break;
		case EAST_SOUTH_EAST:
			l.add(-z,0,x);
			l.add(-x,0,-z);
			l.add(-z,0,x);
			break;
		case SOUTH_EAST:
			l.add(-x,0,-z);
			l.add(-z,0,x);
			break;
		case SOUTH_SOUTH_EAST:
			l.add(-x,0,-z);
			l.add(-x,0,-z);
			l.add(-z,0,x);
			break;
		case SOUTH_SOUTH_WEST:
			l.add(-x,0,-z);
			l.add(-x,0,-z);
			l.add(z,0,-x);
			break;
		case SOUTH_WEST:
			l.add(-x,0,-z);
			l.add(z,0,-x);
			break;
		case WEST_NORTH_WEST:
			l.add(z,0,-x);
			l.add(x,0,z);
			l.add(z,0,-x);
			break;
		case WEST_SOUTH_WEST:
			l.add(z,0,-x);
			l.add(-x,0,-z);
			l.add(z,0,-x);
			break;
		case DOWN:
			l.add(0,-z,0);
			break;
		case UP:
			l.add(0,z,0);
		default:
			l.add(x,0,z);
			break;
		}
		return l;
	}
	
    public int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    
    public Location getCenter(Location loc) {
        return new Location(loc.getWorld(),
            getRelativeCoord(loc.getBlockX()),
            getRelativeCoord(loc.getBlockY()),
            getRelativeCoord(loc.getBlockZ()));
    }
    
    private static double getRelativeCoord(int i) {
        double d = i;
        if(d<0){d+=.5;}else{d+=.5;}
        return d;
    }
    
    @Deprecated
    public short getfromDyeColor(DyeColor c){
    	switch (c) {
		case BLACK: return 0;
		case BLUE: return 4;
		case BROWN: return 3;
		case CYAN: return 6;
		case GRAY: return 8;
		case LIGHT_GRAY: return 7;
		case WHITE: return 15;
		case GREEN: return 2;
		case LIGHT_BLUE: return 12;
		case LIME: return 10;
		case MAGENTA: return 13;
		case ORANGE: return 14;
		case PINK: return 9;
		case PURPLE: return 5;
		case RED: return 1;
		case YELLOW: return 11;
		default: return 15;
		}
    }
    
}
