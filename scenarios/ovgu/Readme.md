Input Data from OvGU Magdeburg
Mainly data of orders from All-You-Need (AYN) delivery service

Files contains (as far as I (KMT) remember) 
xx_Matrix: MAtrix with traveltimes beetween demand locations of requests. Available for three cities/regions in Germany (Hamburg, Muenchen, Ruhrgebiet)
Requests: Information in which of the six available 2h-TimeWindows the order comes. First value: Id<OrderRequest> second and third value: TimeWindow once for destribution of AYN (has peak in the afternoon); once randomly destributed over the day (more constant over timeWindows) 
Sequence_Instances: 1000 predefined sequences of of the incoming 400 order requests (for 1000 runs a 400 order requests)