package parse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class CreateImageList1 {

	private final Integer NUM_OF_IMAGES = 600;
	private final Integer TOTAL_NUM_IMAGES = 1260;
	private final Integer IMAGE_PER_HIT = 25;
	public Integer[] map = new Integer[this.NUM_OF_IMAGES];
	
	public CreateImageList1() {
		// TODO Auto-generated constructor stub
		for(int i=0; i< this.NUM_OF_IMAGES; i++)
		{
			this.map[i]=0;
		}
	}
	
	
	public void PrepareList() throws IOException
	{
		BufferedWriter bw=null;
		String res="";
		try
		{
			bw = new BufferedWriter(new FileWriter(new File("imagelist.txt")));
			for(int j=0; j < this.NUM_OF_IMAGES/this.IMAGE_PER_HIT; j++)
			{
				Random random = new Random(this.TOTAL_NUM_IMAGES);
				for(int k=1; k<=this.IMAGE_PER_HIT; k++)
				{
					Integer val = random.nextInt(this.TOTAL_NUM_IMAGES)%this.IMAGE_PER_HIT;
					System.out.println("FOR");
					while(this.map[val] != 0)
					{
						val = (int) (random.nextInt(this.NUM_OF_IMAGES)*System.currentTimeMillis()%this.NUM_OF_IMAGES);
						System.err.println("LOOP: "+val);
					}	
					this.map[val] =1;
					res+="train_images/"+this.getString(val+1)+".jpg ";
					
				}
				
				bw.write(res.trim()+"\n");
				res="";
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			bw.close();
		}
				
	}
	
	
	public String getString(Integer val)
	{
		  String retVal;
	        int fixedSize = 6;
	        retVal = val + "";
	        char[] array = new char[fixedSize - retVal.length()];
	        Arrays.fill(array, '0');
	        retVal = new String(array) + retVal;
	        System.out.println(retVal);
	        
	        return retVal;
	}

	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		CreateImageList1 createImageList1 = new CreateImageList1();
		createImageList1.PrepareList();
		
	}

}
