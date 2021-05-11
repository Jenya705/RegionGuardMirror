package com.github.jenya705;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValueContainer<T> {

    private T value;

    public ValueContainer() {}

    public ValueContainer(T startValue){
        setValue(startValue);
    }

}
