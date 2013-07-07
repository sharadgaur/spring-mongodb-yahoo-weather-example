function() {
	var key= this.zip+"/"+ this.day;
	var value={
			low	:	this.low,
			high:	this.high,
			avg	:	(this.high+this.low)/2,
			day	:	this.day,
			zip	:	this.zip
	};
	
      emit( key,value); 
}
 