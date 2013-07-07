function(key,values) { 
    var output={day:"",zip:"",low:0,high:0,avg:0,count:0};
    
        output.count=values.length;
        output.zip=values[0].zip;
        output.day=values[0].day;
        output.low=values[0].low;
        output.high=values[0].high;

     for(var i=1;i<values.length;i++){ 
         if(output.low>values[i].low) {
             output.low=values[i].low;
         }
         if(output.high<values[i].high) {
             output.high=values[i].high;
         }
         
     } 
     output.avg=(output.low + output.high)/2; 
     return output; 
 }