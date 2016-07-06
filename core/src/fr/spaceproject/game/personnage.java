package fr.spaceproject.game;

public class personnage {

	private double money;
	private int exp;


	public personnage() {
		money = 10;
		//int exp=0;
	}

	public void addMoney(double money) {
		this.money += money;
	}

	public boolean delMoney(double price) {
		if (money >= price) {
			money -= price;
			return true;
		}
		System.out.println("pas assez d'argent (possession : " + money + " )");
		return false;
	}
	/*
	public void transExp(int exp){
	this.exp+=exp;
	if (exp<0 && exp>-this.exp)
		this.exp-=exp;
	}*/


}
