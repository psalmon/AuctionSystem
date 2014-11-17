A stock auction simulator.
Built to simulate the buying and selling of a single stock, auction style.
Stocks are sold based on lowest price order, with earliest submission used as a tie-breaker. Purchases are made based on earliest submission.
Stock offers are considered until they run out, or until their owner runs out.
Purchases are made until a bid is fulfilled or there are no offers left to satisfy it.

Built to consider input across a length of time, so the appropriate methods should be thread-safe.
By default, it will spawn 3 client threads with 100 of the individual stock to trade. They will each have a pseudo-random bid and offer applied (see usage below).


Usage:
AuctionClientSpawner should be run to test the output of 3 random clients. You can change their bids in AuctionClient.run(), or instantiate more clients with unique names to test additional users.

Sample output:
BIDS
OWNER	QUANTITY	PRICE
bob	       69       43
steve	   62       70
ned	       89       52
OFFERS
OWNER	QUANTITY	PRICE
ned			7		0
bob		   13    	34
steve	   54		34
Cross found, bob buys 7 from ned at 0 per share
Cross found, bob buys 54 from steve at 34 per share
Cross found, ned buys 13 from bob at 34 per share
