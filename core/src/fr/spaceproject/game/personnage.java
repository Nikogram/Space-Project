package fr.spaceproject.game;

public class personnage {	
	
	private int money;
	private int exp;
	
	public personnage(){
		int money=0;
		int exp=0;
	}
	
	public void transMoney(int money){
		this.money+=money;
		if (money<0 && money>-this.money)
			this.money-=money;
	}
		
	public void transExp(int exp){
		this.exp+=exp;
		if (exp<0 && exp>-this.exp)
			this.exp-=exp;
	}
	
	
}
