!init.

+!init 
	: true 
    <- .send("traffic::car::agentcar 0", tell, myvalue(10)); 
    .send("traffic::car::agentcar 1", tell, myvalue(10)); 
    .send("traffic::car::agentcar 5", tell, myvalue(10));
    !init.

