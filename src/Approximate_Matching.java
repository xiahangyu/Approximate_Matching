
import java.io.*;
import java.util.ArrayList;

public class Approximate_Matching {
	public static void main(String[] args){
		ArrayList<String> Pname = new ArrayList<String>(); //Where the Persian names stored.
		ArrayList<String> Lname = new ArrayList<String>(); //Where the corresponding Latin names stored.
		
		//Read in train.txt
		String fpath="./train.txt";
		ReadTextFile(fpath,Pname,Lname);
		
		String resultPath="./results.txt";
		Matching_And_Print(Pname,Lname,resultPath); //Find the best matching Latin name for each Persian name and print out the results in a file with path resultPath
		System.out.println("Done!");
	}
	
	//Read from train.txt and store the names in the first column in Pname, the second in Lname.
	public static void ReadTextFile(String fpath, ArrayList<String> Pname, ArrayList<String> Lname){
		try{
			File file=new File(fpath);
			if(!file.exists()){
				System.out.println("File does not exit!");
				return;
			}
			
			InputStreamReader read=new InputStreamReader(new FileInputStream(file));
			BufferedReader br=new BufferedReader(read);
			
			String str=null;
			while( (str=br.readLine()) !=null ){
				Pname.add(str.split("\t")[0]);
				Lname.add(str.split("\t")[1]);
			}
			
			br.close();
		}
		catch(IOException ex){
			System.out.println(ex.getStackTrace());
		}
	}

	public static void Matching_And_Print(ArrayList<String> Pname,ArrayList<String> Lname, String ResultPath){
		BufferedWriter bw=null;
		try{
			File f=new File(ResultPath);
			OutputStreamWriter write=new OutputStreamWriter(new FileOutputStream(f));
			bw=new BufferedWriter(write);

			long begin=System.currentTimeMillis();
			int hit_predictions=0;	//the number of correct predictions
			int total_predictions=0;	//the number of predictions
			int hit_names=0;	//the number of names with a correct prediction
			int total_names=Pname.size();	
			for(int i=0; i<total_names; i++){//Find the closest Latin name for each Persian name in global edit distance.
				//int min_index=-1;	//the index of Latin name that is the closest to ith Persian name
				int min_distance=Integer.MAX_VALUE;	//the minimum distance between ith Persian name and the Latin name with the index of min_index 
				int distance;
				
				ArrayList<String> matched_Lnames=new ArrayList<String>(); //the indexes of ith Latin names that are the closest to ith Persian name
				for(int j=0;j<Lname.size();j++){//Compute the min_index and min_distance for each Persian name in the list
					distance=GED_Method.getDistance( Pname.get(i).toLowerCase(), Lname.get(j), 1); //Compute the distance between ith Persian name and jth Latin name.
					if(distance<min_distance){	//If jth Latin name has the closest distance to ith Persian name currently
						matched_Lnames.clear();
						min_distance=distance;
						matched_Lnames.add(Lname.get(j));
					}
					else if(distance==min_distance){	//If the jth Latin name is one of the closest names to ith Persian name, then add it to the queue matched_Lnames_index.
						int l;
						//Make sure the jth Latin name has not already exited in the queue before add it.
						for( l=0; l<matched_Lnames.size() ;l++){
							if( matched_Lnames.get(l).equals( Lname.get(j) ))
								break;
						}
						if(l==matched_Lnames.size())
							matched_Lnames.add(Lname.get(j));
					}
				}
				total_predictions+=matched_Lnames.size();

				ArrayList<String> correct_Lnames=new ArrayList<String>();	//All the correct Latin names for ith Persian name
				for(int l=0;l<Pname.size();l++){
					if(Pname.get(i).equals(Pname.get(l))){
						correct_Lnames.add(Lname.get(l));
					}
				}
				
				boolean hit=false;
				bw.write(Pname.get(i) + " hits: " );
				for( int k=0;k<matched_Lnames.size();k++){	//Evaluation
					for(int z=0;z<correct_Lnames.size();z++){
						if( matched_Lnames.get(k).equals(correct_Lnames.get(z))){//If the Persian names with the index of  min_index and i are equal, the approximate matching hits a correct result.  
							hit_predictions++;
							hit=true;
							bw.write("! ");
						}
					}
					bw.write( matched_Lnames.get(k) + ", ");
				}
				bw.newLine();
				if(hit){
					hit_names++;
				}
			}
			long end=System.currentTimeMillis();

			int precision=hit_predictions*100/total_predictions;
			int recall=hit_names*100/total_names;
			bw.write("Precision: " + precision + "% 	correct predictions:" + hit_predictions + " , total predictions:" + total_predictions);
			bw.newLine();
			bw.write("Recall: " + recall + "% 	 names with correct prediction:" + hit_names + " , total names:" + total_names);
			bw.newLine();
			bw.write("Program running time: " + (end-begin)+ " milliseconds!");
			bw.newLine();
			bw.close();
		}
		catch(IOException ex){
			System.out.println(ex.getStackTrace());
		}
	}

}