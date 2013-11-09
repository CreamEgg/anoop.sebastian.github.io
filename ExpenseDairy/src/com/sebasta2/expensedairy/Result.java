package com.sebasta2.expensedairy;

import android.graphics.Bitmap;

public class Result {
	String date;
	String elecamount; 	String eleccomment;  Bitmap elecreceipt;
	String grocamount;	String groccomment;  Bitmap grocreceipt;
	String insuamount;	String insucomment;  Bitmap insureceipt;
	String interamount;	String intercomment; Bitmap interreceipt;
	String leisamount;	String leiscomment;  Bitmap leisreceipt;
	String loanamount;	String loancomment;  Bitmap loanreceipt;
	String otheamount;	String othecomment;  Bitmap othereceipt;
	String rentamount;	String rentcomment;  Bitmap rentreceipt;
	String teleamount;	String telecomment;  Bitmap telereceipt;
	String tranamount;	String trancomment;  Bitmap tranreceipt;
	String utilamount;	String utilcomment;  Bitmap utilreceipt;
	
	Result(String date,String elecamount,String eleccomment,Bitmap elecreceipt,
			String grocamount,	String groccomment,  Bitmap grocreceipt,
			String insuamount,	String insucomment,  Bitmap insureceipt,
			String interamount,	String intercomment, Bitmap interreceipt,
			String leisamount,	String leiscomment,  Bitmap leisreceipt,
			String loanamount,	String loancomment,  Bitmap loanreceipt,
			String otheamount,	String othecomment,  Bitmap othereceipt,
			String rentamount,	String rentcomment,  Bitmap rentreceipt,
			String teleamount,	String telecomment,  Bitmap telereceipt,
			String tranamount,	String trancomment,  Bitmap tranreceipt,
			String utilamount,	String utilcomment,  Bitmap utilreceipt) {

		this.date = date;
		this.elecamount = elecamount; 	this.eleccomment = eleccomment ;  this.elecreceipt = elecreceipt;
		this.grocamount = grocamount;	this. groccomment = groccomment ; this.grocreceipt = grocreceipt;
		this.insuamount=insuamount;	    this.insucomment = insucomment;   this.insureceipt = insureceipt;
		this.interamount=  interamount;	this.intercomment = leiscomment;  this.interreceipt = interreceipt;
		this.leisamount =leisamount;	this.leiscomment = leiscomment;   this.leisreceipt = leisreceipt;
		this.loanamount = loanamount;	this.loancomment = loancomment;   this.loanreceipt = loanreceipt;
		this.otheamount = otheamount;	this.othecomment = othecomment;   this.othereceipt = othereceipt;
		this.rentamount = rentamount;	this.rentcomment = rentcomment;   this.rentreceipt = othereceipt;
		this.teleamount = teleamount;	this.telecomment = telecomment;   this.telereceipt = telereceipt;
		this.tranamount = tranamount;	this.trancomment = trancomment;   this.tranreceipt = tranreceipt;
		this.utilamount = utilamount;	this.utilcomment = utilcomment;   this.utilreceipt = utilreceipt;
	}	
}
