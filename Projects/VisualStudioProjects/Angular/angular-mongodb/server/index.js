var config=require("./config.json");



module.exports=function(app){
  app.get('/api/tedData', async (req, res,next)=> {
    try{
      const db = req.app.locals.db;
      const perPage = 100;
    //  var page = parseInt(req.query.page);
    var page=0; 
    page+=1;
      const tedData = await db.collection(config.db.connection.tedCollection).find({}).skip((perPage * page) - perPage).limit(perPage).toArray();
        if(tedData){
          res.send(tedData);
        }
        else{
          res.sendStatus(404);
        }
    }
    catch(err){
        next(err);
    }
  });
   
};
