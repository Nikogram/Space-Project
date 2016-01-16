package fr.spaceproject.utils;

public class Coor {
	private String str;
	
	public Coor(){
		str="";
	}
	public Coor(int i,int j){
		str=i +" "+j;
	}
	public Coor(String txt){
		str=txt;
	}
	public int getX(){
		String[] valeur = str.split(" ");
		return Integer.parseInt(valeur[0]);
	}
	public int getY(){
		String[] valeur = str.split(" ");
		return Integer.parseInt(valeur[1]);
	}
	public String addXY(int i,int j){
		int newx = getX()+i;
		int newy = getY()+j;
		return newx + " "+newy;
	}
	public String toStrings(){
		return str=getX() +" "+getY();
	}
}
	

