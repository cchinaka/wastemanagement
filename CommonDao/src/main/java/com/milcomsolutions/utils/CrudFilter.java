package com.milcomsolutions.utils;

public class CrudFilter {

    // @RequestParam(value = "draw", required = false) Integer pageNo
    private Integer draw;

    // @RequestParam(value = "length", required = false) Integer pageSize
    private Integer length;

    // @RequestParam(value = "start", required = false) Integer startIndex
    private Integer start;

    // @RequestParam(value = "search[value]", required = false) String searchValue
    private String value;


    public Integer getDraw() {
        return draw;
    }


    public void setDraw(Integer draw) {
        this.draw = draw;
    }


    public Integer getLength() {
        return length;
    }


    public void setLength(Integer length) {
        this.length = length;
    }


    public Integer getStart() {
        return start;
    }


    public void setStart(Integer start) {
        this.start = start;
    }


    public String getValue() {
        return value;
    }


    public void setValue(String searchValue) {
        this.value = searchValue;
    }


    public String toString() {
        return String.format("draw: %s, length: %s, start: %s, value: %s", draw, length, start, value);
    }
}
