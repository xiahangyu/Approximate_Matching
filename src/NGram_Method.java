import java.util.ArrayList;

public class NGram_Method {
	public static int getDistance(String Resource, String Target, int n){
		int lr=Resource.length();
		int lt=Target.length();
		if(n<=0){
			System.out.println("Invalid n!");
			System.exit(-1);
		}
		else if(n>lr || n>lt){
			n= lr>lt?lt:lr;
		}
		
		int Gr=2+lr+1-n;	//Gr=|Gn(Resource)|
		int Gt=2+lt+1-n;	//Gt=|Gn(Target)|
		int Grt=0;	//Grt=|Gn(Resource)and Gn(Target)|
		
		if(Resource.substring(0,n-1).equals( Target.substring(0,n-1) )){
			Grt++;
		}
		if( Resource.substring(lr-n+1,lr).equals(Target.substring(lt-n+1,lt)) )
			Grt++;
		
		ArrayList<String> ReSubStr=new ArrayList<String>();
		ArrayList<String> TarSubStr=new ArrayList<String>();
		for(int i=0;i<lr-n;i++){
			ReSubStr.add(Resource.substring(i,i+n));
		}
		for(int j=0;j<lt-n;j++){
			TarSubStr.add(Target.substring(j,j+n));
		}
		
		for(int i=0;i<ReSubStr.size();i++){
			for(int j=0;j<TarSubStr.size();j++){
				if(ReSubStr.get(i).equals(TarSubStr.get(j))){
					Grt++;
					TarSubStr.remove(j);
				}
			}
		}
		
		return Gr+Gt-2*Grt;
	}
}
