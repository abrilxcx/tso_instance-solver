import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.text.DecimalFormat;
import java.util.*;

public class InstanceSolver
{
	DecimalFormat df = new DecimalFormat("#.00");
	DecimalFormat df2 = new DecimalFormat("#.0000");
	ArrayList<String> lines = new ArrayList<String>();
	int[] customx = new int[110];
    int[] customy = new int[110];
    int[][] rv = new int[12][110];
    int[][] ls = new int[12][110];
    int[] it = new int[30];
    double[][] d = new double[110][110];
    double[][] dt = new double[110][110];
    double[] chrdistance = new double[20];
	int customers, vehicles, depotx, depoty, i, j, z, a = 1, v = 1, w = 1, minori = 0, minorj = 0, k, p, x, y, temp, cont;
	double distance, minor = 0, tdistance, lsdistance, lsrdistance, imp;
	
	public void Instance(String file){
		//Open the file
		try
        {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String bfRead;
            while ( (bfRead = bufferedReader.readLine()) != null )
            {
                lines.add(bfRead);
            }
        }

        catch ( Exception e )
        {
            System.err.println("File not found: " + e);
        }

        //Get data of the instance
		String tcustom = lines.get(3);
        String[] arrcustom = tcustom.split(":");
        arrcustom[1] = arrcustom[1].trim();
        customers = (Integer.parseInt(arrcustom[1]));

        String tvehicles = lines.get(0);
        String[] arrvehicles = tvehicles.split("k");
        arrvehicles[1] = arrvehicles[1].trim();
        vehicles = Integer.parseInt(arrvehicles[1]);

        String tdepot = lines.get(7);
        String[] arrdepot = tdepot.split(" ", 5);
        arrdepot[1] = arrdepot[1].trim();
        depotx = Integer.parseInt(arrdepot[2]);
        depoty = Integer.parseInt(arrdepot[3]);

        j = 0;
        z = 6 + customers;

        for (int i = 7; i <= z; i++) {
        	String tcoord = lines.get(i);
	        String[] arrcoord = tcoord.split(" ", 5);
	        arrcoord[1] = arrcoord[1].trim();
	        customx[j] = Integer.parseInt(arrcoord[2]);
	        customy[j] = Integer.parseInt(arrcoord[3]);
	        j++;
        }

        //Print data
        System.out.println("\nNumber of Customers (including depot): " + customers);
        System.out.println("Number of Vehicles: " + vehicles);
        System.out.println("Depot coordinates: " + depotx + " " + depoty);
        System.out.println("\nCustomer coordinates: ");

        j = 1;
        for (i = 1; i < customers+1; i++)
		{
              System.out.println("Customer " + j + ": " + customx[i-1] + " " + customy[i-1]);
              j++;   
		}

		//Distance calculation
		for (i = 1; i <= customers; i++) {
			for (j = 1; j <= customers; j++) {
				distance = (Math.pow((customx[j-1] - customx[i-1]), 2)) + (Math.pow((customy[j-1] - customy[i-1]), 2));
				d[i][j] = Math.sqrt(distance);
				dt[i][j] = Math.sqrt(distance);
			}
		}
		System.out.println("\nDistance calculation done ");

		double startTime = System.nanoTime();
		//Calculate routes
		while(a < customers)
		{
            if (w==1)
            {
                it[v]=1;         
            }
            i = it[v];

			for (j = 1; j <= customers; j++) 
			{
				if (minor == 0) 
				{
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

			for (k = 1; k <= customers; k++) 
			{
				d[k][minorj] = 0;
                d[k][minori] = 0;
			}

			rv[v][w] = minorj;
			it[v] = minorj;
			v++;

			if (v > vehicles) {
				v = 1;
				w++;
			}
			a++;
		}
		System.out.println("Heuristic done");

		//Calculate the total distance
		for(i = 1; i <= vehicles; i++)
		{
			for(j = 1; j <= w; j++)
			{
				if(j == 1)
				{
					tdistance += dt[1][rv[i][j]];
				}
				else
				{
					if(rv[i][j] != 0 && rv[i][j+1] != 0)
					{
						tdistance += dt[rv[i][j]][rv[i][j+1]];
					}
				}
			}
		}

		for (i = 1; i <= vehicles; i++) 
		{
			for (j = 1; j <= w; j++) 
			{
				if(rv[i][j+1] == 0)
				{
					tdistance += dt[rv[i][j]][1];
				}
			}
		}
		double endTime = System.nanoTime();
		double totalTime = (endTime - startTime);
		System.out.println("Total Distance done");


		//Print routes
		System.out.println("\n================ Solution ================");

		for (x = 1; x <= vehicles; x++)
		{
                    System.out.print("\nRoute " + x + ": " + "1" + " ");
                    for (y = 1; y <= w; y++){
                        if (rv[x][y] != 0)
                        {
                            System.out.print(rv[x][y] + " ");
                        }
                    }
                    System.out.print("1");           
		}
		System.out.print("\n\nTotal distance: " + df.format(tdistance) + " km");

		double startTimeLS = System.nanoTime();
		//Local search
		for (i = 1; i <= vehicles; i++) 
		{
			for (j = 1; j <= w; j++) 
			{
				if(rv[i][j] != 0)
				{
					ls[i][j] = rv[i][j];
				}
			}
		}
		System.out.println("\n\nLS input array done\n");

		//Calculate distance per route
		for(i = 1; i <= vehicles; i++)
		{
			for(j = 1; j <= w; j++)
			{
				if(j == 1)
				{
					chrdistance[i] += dt[1][ls[i][j]];
				}
				else
				{
					if(ls[i][j] != 0 && ls[i][j+1] != 0)
					{
						chrdistance[i] += dt[ls[i][j]][ls[i][j+1]];
					}
				}
			}
		}
		for (i = 1; i <= vehicles; i++) 
		{
			for (j = 1; j <= w; j++) 
			{
				if(ls[i][j+1] == 0)
				{
					chrdistance[i] += dt[ls[i][j]][1];
				}
			}
		}

		//LS move
		for(i = 1; i <= vehicles; i++)
		{
			cont = 0;
			do
			{
				lsrdistance = 0;
				for(j = 1; j <= w; j++)
				{
					if(ls[i][j] != 0 && ls[i][j+1] != 0)
					{
						temp = ls[i][j];
						ls[i][j] = ls[i][j+1];
						ls[i][j+1] = temp;
					}
				}
				for(j = 1; j <= w; j++)
				{
					if(j == 1)
					{
						lsrdistance += dt[1][ls[i][j]];
					}
					else
					{
						if(ls[i][j] != 0 && ls[i][j+1] != 0)
						{
							lsrdistance += dt[ls[i][j]][ls[i][j+1]];
						}
					}
				}
				for (j = 1; j <= w; j++) 
				{
					if(ls[i][j+1] == 0)
					{
						lsrdistance += dt[ls[i][j]][1];
					}
				}
				cont++;
			}while(lsrdistance > chrdistance[i]);

			System.out.println("Number of moves in R" + i + ": " + cont);
		}
		System.out.println("\nLS move done");

		//Calculte total LS distance
		for(i = 1; i <= vehicles; i++)
		{
			for(j = 1; j <= w; j++)
			{
				if(j == 1)
				{
					lsdistance += dt[1][ls[i][j]];
				}
				else
				{
					if(ls[i][j] != 0 && ls[i][j+1] != 0)
					{
						lsdistance += dt[ls[i][j]][ls[i][j+1]];
					}
				}
			}
		}
		for (i = 1; i <= vehicles; i++) 
		{
			for (j = 1; j <= w; j++) 
			{
				if(ls[i][j+1] == 0)
				{
					lsdistance += dt[ls[i][j]][1];
				}
			}
		}
		double endTimeLS = System.nanoTime();
		double totalTimeLS = (endTimeLS - startTimeLS);
		System.out.println("LS total distance done");

		System.out.println("\n=============== LS Solution ===============");
		for (x = 1; x <= vehicles; x++)
		{
                    System.out.print("\nRoute " + x + ": " + "1" + " ");
                    for (y = 1; y <= w; y++){
                        if (ls[x][y] != 0)
                        {
                            System.out.print(ls[x][y] + " ");
                        }
                    }
                    System.out.print("1");           
		}
		System.out.print("\n\nTotal Local Search distance: " + df.format(lsdistance) + " km");
		System.out.print("\nTotal Constructive Heuristic distance: " + df.format(tdistance) + " km");
		imp = ((tdistance - lsdistance) / tdistance) * 100;
		System.out.println("\nImprovement Rate: " + df.format(imp) + "%");

		totalTime /= 1e6;
		totalTimeLS /= 1e6;
		System.out.print("\nTime elapsed CH: " + totalTime + " ms");
		System.out.print("\nTime elapsed LS: " + totalTimeLS + " ms");
	}

	public static void main(String[] args) 
	{
		Scanner s = new Scanner(System.in);

		String instancename;
		System.out.println("Enter the name of the file to read: ");
        instancename = s.nextLine();

        InstanceSolver file = new InstanceSolver();

        file.Instance(instancename);
	}
}