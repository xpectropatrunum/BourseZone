package ir.sourcearena.boursezone.dbhelper;

public class Filter {
    int _id, type;
    String _name;
    String _function;
    public Filter(){   }
    public Filter(int id, String name, String _function,int type){
        this._id = id;
        this._name = name;
        this._function = _function;
        this.type = type;

    }

    public Filter(String name, String _function,int type){
        this._name = name;
        this._function = _function;
        this.type = type;
    }
    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }
    public int getType(){
        return this.type;
    }

    public void setType(int id){
        this.type = type;
    }

    public String getName(){
        return this._name;
    }

    public void setName(String name){
        this._name = name;
    }

    public String getFunctionr(){
        return this._function;
    }

    public void setFunction(String _function){
        this._function = _function;
    }
}