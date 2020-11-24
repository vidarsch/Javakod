import java.lang.*;
import java.lang.Math;

public class Triangel {
	double oneSide;
	double twoSide;
	double threeSide;
		
		
	public Triangel(double aSide,double bSide, double cSide) {
		oneSide = aSide;
		twoSide = bSide;
		threeSide = cSide;
	}
		
	public double bisektris (String selection) {
		double b = 0;
		double c = 0;
		double bis;
			
		if (selection == "110") {
			 b = oneSide; 
			 c = twoSide;}
		else if (selection == "101") {
			 b = oneSide;
			 c = threeSide;
		} else if (selection == "011") {
			 b = twoSide;
			 c = threeSide;
		} else { 
			 b = 0;
			 c = 0;
			return 0;
		}
		
		if (b>=c) {
			double alfa = Math.acos(c/b);
			double p = 2 * b * c * Math.cos (alfa / 2);
			
		} else {
			double alfa = Math.acos(b/c);
			double p = 2 * b * c * Math.cos (alfa / 2);
			
		}
	bis = p / (b + c);
		
	return bis;
			
	}
		
	public double omkrets() {
		return oneSide + twoSide + threeSide;
			
			
	}
	
	public static void main(String[] args) {
			
		Triangel myTriangle = new Triangel(2,4,2);
		System.out.println(myTriangle.omkrets());
		System.out.println(myTriangle.bisektris("110"));
			
	}
	
	
}
