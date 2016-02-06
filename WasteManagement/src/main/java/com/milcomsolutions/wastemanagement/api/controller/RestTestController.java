package com.milcomsolutions.wastemanagement.api.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.milcomsolutions.web.controller.BaseController;


@RestController
@RequestMapping("/rest")
public class RestTestController extends BaseController {

    @RequestMapping(value = "/inflow-total", method = RequestMethod.GET)
    public BigDecimal getTotalInflow() {
        return new BigDecimal(10000206593.9d);
    }


    @RequestMapping(value = "/inflow-to-date", method = RequestMethod.GET)
    public Map<String, Object> calculateInflowToDate() {
        Map<String, Object> inflow = new HashMap<String, Object>();
        inflow.put("ytdPercentage", 3.75d);
        inflow.put("ytdAmount", 503756d);
        inflow.put("mtdPercentage", -1.09);
        inflow.put("mtdAmount", 103104);
        inflow.put("wtdPercentage", -1.8);
        inflow.put("wtdAmount", 20109);
        inflow.put("todayPercentage", 20.41);
        inflow.put("todayAmount", 11054);
        return inflow;
    }


    @RequestMapping(value = "/{number}/inflow-top-mdas", method = RequestMethod.GET)
    public List<Map<String, Object>> buildInflowTopMDAs(@PathVariable Long number) {
        List<Map<String, Object>> topMdas = new ArrayList<>();
        topMdas.add(getNameAmountMap("Chukason and Sons Stores", 15000000.00d));
        topMdas.add(getNameAmountMap("Joe Boutique", 10500000.00d));
        topMdas.add(getNameAmountMap("Mujib's Karate School, and general mechandizing Okota", 9500000d));
        topMdas.add(getNameAmountMap("Eyo Computer School", 8500000d));
        topMdas.add(getNameAmountMap("Busola newbies are us Intl.", 7300000d));
        return topMdas;
    }


    @RequestMapping(value = "/{number}/inflow-bottom-mdas", method = RequestMethod.GET)
    public List<Map<String, Object>> buildInflowBottomMDAs(@PathVariable Long number) {
        List<Map<String, Object>> topMdas = new ArrayList<>();
        topMdas.add(getNameAmountMap("Mikey Pawn shop", 50000.00d));
        topMdas.add(getNameAmountMap("Otun Containerized Fitness Dishes", 40000.00d));
        topMdas.add(getNameAmountMap("Lanre Code Cateering", 30100d));
        topMdas.add(getNameAmountMap("Eze and Sons Shoe Shop", 10000d));
        topMdas.add(getNameAmountMap("Blessed Cinemas", 3100d));
        return topMdas;
    }


    @RequestMapping(value = "/inflow-graph", method = RequestMethod.GET)
    public Map<String, Object> inflowGraph() {
        Map<String, Object> graph = new HashMap<>();
        List<String> labels = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November",
                "December");
        List<Map<String, Object>> dataSets = new ArrayList<>();
        List<Number> data = Arrays.asList(456, 479, 324, 569, 702, 600, 800, 233, 433, 533, 322, 532);
        dataSets.add(buildData("#3498db", "#48A4D1", data));
        graph.put("labels", labels);
        graph.put("datasets", dataSets);
        return graph;
    }


    @RequestMapping(value = "/outflow-total", method = RequestMethod.GET)
    public BigDecimal getTotalOutflow() {
        return new BigDecimal(10000206593.9d);
    }


    @RequestMapping(value = "/outflow-to-date", method = RequestMethod.GET)
    public Map<String, Object> calculateOutflowToDate() {
        Map<String, Object> outflow = new HashMap<String, Object>();
        outflow.put("ytdPercentage", 3.75d);
        outflow.put("ytdAmount", 503756d);
        outflow.put("mtdPercentage", -1.09);
        outflow.put("mtdAmount", 103104);
        outflow.put("wtdPercentage", -1.8);
        outflow.put("wtdAmount", 20109);
        outflow.put("todayPercentage", 20.41);
        outflow.put("todayAmount", 11054);
        return outflow;
    }


    @RequestMapping(value = "/{number}/outflow-top-mdas", method = RequestMethod.GET)
    public List<Map<String, Object>> buildOutflowTopMDAs(@PathVariable Long number) {
        List<Map<String, Object>> topMdas = new ArrayList<>();
        topMdas.add(getNameAmountMap("Chukason and Sons Stores", 15000000.00d));
        topMdas.add(getNameAmountMap("Joe Boutique", 10500000.00d));
        topMdas.add(getNameAmountMap("Mujib's Karate School, and general mechandizing Okota", 9500000d));
        topMdas.add(getNameAmountMap("Eyo Computer School", 8500000d));
        topMdas.add(getNameAmountMap("Busola newbies are us Intl.", 7300000d));
        return topMdas;
    }


    @RequestMapping(value = "/{number}/outflow-bottom-mdas", method = RequestMethod.GET)
    public List<Map<String, Object>> buildOutflowBottomMDAs(@PathVariable Long number) {
        List<Map<String, Object>> topMdas = new ArrayList<>();
        topMdas.add(getNameAmountMap("Mikey Pawn shop", 50000.00d));
        topMdas.add(getNameAmountMap("Otun Containerized Fitness Dishes", 40000.00d));
        topMdas.add(getNameAmountMap("Lanre Code Cateering", 30100d));
        topMdas.add(getNameAmountMap("Eze and Sons Shoe Shop", 10000d));
        topMdas.add(getNameAmountMap("Blessed Cinemas", 3100d));
        return topMdas;
    }


    @RequestMapping(value = "/outflow-graph", method = RequestMethod.GET)
    public Map<String, Object> outflowGraph() {
        Map<String, Object> graph = new HashMap<>();
        List<String> labels = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November",
                "December");
        List<Map<String, Object>> dataSets = new ArrayList<>();
        List<Number> data = Arrays.asList(456, 479, 324, 569, 702, 600, 800, 233, 433, 533, 322, 532);
        dataSets.add(buildData("grey", "grey", data));
        graph.put("labels", labels);
        graph.put("datasets", dataSets);
        return graph;
    }


    private Map<String, Object> buildData(String fillColor, String strokeColor, List<? extends Number> data) {
        Map<String, Object> dataset = new HashMap<String, Object>();
        dataset.put("fillColor", fillColor);
        dataset.put("strokeColor", strokeColor);
        dataset.put("data", data);
        return dataset;
    }


    @RequestMapping(value = "/efficiency-total", method = RequestMethod.GET)
    public BigDecimal getTotalEfficiency() {
        return new BigDecimal(17.20);
    }


    @RequestMapping(value = "/efficiency-to-date", method = RequestMethod.GET)
    public Map<String, Object> calculateEfficiencyToDate() {
        Map<String, Object> efficiency = new HashMap<String, Object>();
        efficiency.put("ytdPercentage", 3.75d);
        efficiency.put("ytdAmount", 503756d);
        efficiency.put("mtdPercentage", -1.09);
        efficiency.put("mtdAmount", 103104);
        efficiency.put("wtdPercentage", -1.8);
        efficiency.put("wtdAmount", 20109);
        efficiency.put("todayPercentage", 20.41);
        efficiency.put("todayAmount", 11054);
        return efficiency;
    }


    @RequestMapping(value = "/{number}/efficiency-top-mdas", method = RequestMethod.GET)
    public List<Map<String, Object>> buildEfficiencyTopMDAs(@PathVariable Long number) {
        List<Map<String, Object>> topMdas = new ArrayList<>();
        topMdas.add(getNameAmountMap("Chukason and Sons Stores", 15000000.00d));
        topMdas.add(getNameAmountMap("Joe Boutique", 10500000.00d));
        topMdas.add(getNameAmountMap("Mujib's Karate School, and general mechandizing Okota", 9500000d));
        topMdas.add(getNameAmountMap("Eyo Computer School", 8500000d));
        topMdas.add(getNameAmountMap("Busola newbies are us Intl.", 7300000d));
        return topMdas;
    }


    @RequestMapping(value = "/{number}/efficiency-bottom-mdas", method = RequestMethod.GET)
    public List<Map<String, Object>> buildEfficiencyBottomMDAs(@PathVariable Long number) {
        List<Map<String, Object>> topMdas = new ArrayList<>();
        topMdas.add(getNameAmountMap("Mikey Pawn shop", 50000.00d));
        topMdas.add(getNameAmountMap("Otun Containerized Fitness Dishes", 40000.00d));
        topMdas.add(getNameAmountMap("Lanre Code Cateering", 30100d));
        topMdas.add(getNameAmountMap("Eze and Sons Shoe Shop", 10000d));
        topMdas.add(getNameAmountMap("Blessed Cinemas", 3100d));
        return topMdas;
    }


    @RequestMapping(value = "/efficiency-graph", method = RequestMethod.GET)
    public Map<String, Object> efficiencyGraph() {
        Map<String, Object> graph = new HashMap<>();
        List<String> labels = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November",
                "December");
        List<Map<String, Object>> dataSets = new ArrayList<>();
        List<Number> data = Arrays.asList(456, 479, 324, 569, 702, 600, 800, 233, 433, 533, 322, 532);
        dataSets.add(buildData("#58b62c", "#58b62c", data));
        graph.put("labels", labels);
        graph.put("datasets", dataSets);
        return graph;
    }


    @RequestMapping(value = "/account-balance", method = RequestMethod.GET)
    public Number getAccountBalance() {
        return new BigDecimal(2000020593.90d);
    }


    private Map<String, Object> getNameAmountMap(String name, Number amount) {
        Map<String, Object> info = new HashMap<>();
        info.put("name", name);
        info.put("amount", String.valueOf(amount));
        return info;
    }
}
