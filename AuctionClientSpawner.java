/**
 * AuctionClientSpawner	Controlling class to start up the system. As is, it will spawn 3 threads to test the auction server.
 * Just run it to test with random values. See ReadMe for more usage information.
 * @author          Paul Salmon
 */

public class AuctionClientSpawner {

	public static void main(String[] args){
		AuctionServer.startAuction(0.00010000);//hours to run.
		
		//spawn some threads for testing.
		//each thread could be taking input from another source until the end of the auction time.
		AuctionClient ac1 = new AuctionClient("bob");
		AuctionClient ac2 = new AuctionClient("ned");
		AuctionClient ac3 = new AuctionClient("steve");

		while(true){//wait for the auction to end.
			try {Thread.sleep(1000);//TODO: wait 1 seconds before checking again. could do some math here to optimize it.
			} catch (InterruptedException e) {e.printStackTrace();}
			if(!(AuctionServer.isLive())){
				AuctionServer.EndAuction();
				break;
			}
		}

	}

}
