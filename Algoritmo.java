
import java.util.Scanner;

public class Algoritmo{

	public static void main(String[] args) 
	{
		int a=1, v=1, vehicles=2, customers=5, w=1, i, j, minori=0, minorj=0, k;
                float minor=0;
		int[][] rv = new int[12][100];
		float[][] d = new float[80][80];
		int[] it = new int[12];
                
                Scanner Leer= new Scanner(System.in);
                for (int x = 1; x <= customers; x++)
		{
                    for (int y = 1; y <= customers; y++){
                        d[x][y]= Leer.nextFloat();
                    }
		}
                             
                for (int x = 1; x <= customers; x++)
		{
                    for (int y = 1; y <= customers; y++){
                         System.out.print(d[x][y]);
                    }
		}
                
		while(a < customers)
		{
                    if (w==1){
                        it[v]=1;         
                    }
                    i = it[v];

			for (j = 1; j <= customers; j++) 
			{
				if (minor == 0) {
					minor = d[i][j];
					minori = i;
					minorj = j;
				}

				if (d[i][j] < minor && d[i][j] != 0) {
					minor = d[i][j];
					minori = i;
					minorj = j;
				}
			}

			minor = 0;

			for (k = 1; k <= customers; k++) {
				d[k][minorj] = 0;
                                d[k][minori] = 0;
			}

			rv[v][w] = minorj;
			v++;

			if (v > vehicles) {
				v = 1;
				w++;
			}
			it[v] = minorj;
			a++;
		}

		for (int x = 1; x <= vehicles; x++)
		{
                    System.out.print("Route " + x + ": " );
                    for (int y = 1; y <= w; y++){
                        System.out.print(rv[x][y] + ", ");
                    }
		}
	}
}
			}
			
			it[v] = minorj;
			a++;
		}

		for (int x = 0; x < rv.length; x++)
		{
        	for (int y = 0; y < rv[x].length; y++)
              System.out.print("Route " + rv[x] + ": " + rv[y]);   
		}
	}
}


