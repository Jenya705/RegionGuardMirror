package com.github.jenya705.command;

import lombok.Getter;

import java.util.Iterator;

@Getter
public class ArgumentsIterator implements Iterator<String> {

    private final String[] args;
    private int current;

    public ArgumentsIterator(String[] args){
        this.args = args;
        current = 0;
    }

    @Override
    public boolean hasNext() {
        return hasNext(1);
    }

    public boolean hasNext(int i){
        return args.length - current - i >= 0;
    }

    @Override
    public String next() {
        if (hasNext()) return args[current++];

        throw new IllegalStateException("Args length is lower or equals with Current");
    }
}
