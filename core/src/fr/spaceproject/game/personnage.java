package fr.spaceproject.game;

public class personnage {

	private int money;
	private int exp;


	public personnage() {
		int money = 0;
		//int exp=0;
	}

	public void addMoney(int money) {
		this.money += money;
	}

	public boolean delMoney(int price) {
		if (money > price) {
			money -= price;
			return true;
		}
		else
			return false;
	}
	/*
	public void transExp(int exp){
	this.exp+=exp;
	if (exp<0 && exp>-this.exp)
		this.exp-=exp;
	}*/


}
