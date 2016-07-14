package de.hhu.imtgg.objects;

public class TDDTuple {
    long seconds;
    String task;

    public TDDTuple(long second, String tasks){
        seconds = second;
        task = tasks;

    }

    public void setSeconds(int set){
        seconds = set;
    }

    public void setTask(String set){
        task = set;
    }

    public long getSeconds(){
        return seconds;
    }

    public String getTask(){
        return task;
    }

}
