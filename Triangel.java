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
		double p;
			
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
			p = 2 * b * c * Math.cos (alfa / 2);
			
		} else {
			double alfa = Math.acos(b/c);
			p = 2 * b * c * Math.cos (alfa / 2);
			
		}
	bis = p / (b + c);
		
	return bis;
			
	}
		
	public static double omkrets(double oA, double oB, double oC) {
		return oA+oB+oC;
			
	}
	
	public double height() {
		double Aa = oneSide;
		double Bb = twoSide;
		double Cc = threeSide;
		double Length = 0;
		
		if (Aa>Bb && Aa>Cc) { 
			Length = bisektris("011");
		}
		if (Bb>Cc && Bb>Aa) { 
			Length = bisektris("101");
		}
		if (Cc>Bb && Cc>Aa) { 
			Length = bisektris("110");
		}
		return Length;
	}
	
	public static void main(String[] args) {
			
		Triangel myTriangle = new Triangel(4,3,2);
		System.out.println("Omkretsen är :"+ myTriangle.omkrets(myTriangle.oneSide,myTriangle.twoSide,myTriangle.threeSide));
		System.out.println("Sidorna på triangeln är :" + myTriangle.oneSide + ", " + myTriangle.twoSide + ", " + myTriangle.threeSide);
		System.out.println("Bisektrisen mellan sidan A och B är : " + myTriangle.bisektris("110"));
		System.out.println("height is : " + myTriangle.height());
			
	}
	
	
}
