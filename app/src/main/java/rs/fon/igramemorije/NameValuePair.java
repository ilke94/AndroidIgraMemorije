package rs.fon.igramemorije;

/**
 * Created by Milos on 2/19/2017.
 */

public class NameValuePair <ValueType extends Comparable<ValueType>> implements Comparable<NameValuePair<ValueType>>{

    private String name;
    private ValueType value;

    public NameValuePair(String _name,ValueType _value){
        name=_name;
        value=_value;
    }

    public String getName(){
        return name;
    }

    public ValueType getValue(){
        return value;
    }
    @Override
    public int compareTo(NameValuePair<ValueType> other) {
        return this.value.compareTo(other.value);
    }
}
