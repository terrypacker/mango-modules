//Add configs to global config that the controller will load (not required)
var amChartConfigs = [];
amChartConfigs['myFirstChart'] =  {                    
        type: "gauge",
        pathToImages: "/resources/amcharts/images/",
        marginBottom: 20,
        marginTop: 40,
        fontSize: 13,
        theme: "dark",
        arrows: [
            {
                id: "GaugeArrow-1",
                value: 0
            }
        ],
        axes: [
            {
                axisThickness: 1,
                bottomText: "",
                bottomTextYOffset: -20,
                endValue: 220,
                id: "GaugeAxis-1",
                valueInterval: 10,
                bands: [
                    {
                        alpha: 0.7,
                        color: "green",
                        endValue: 90,
                        id: "GaugeBand-1",
                        startValue: 0
                    },
                    {
                        alpha: 0.7,
                        color: "yellow",
                        endValue: 130,
                        id: "GaugeBand-2",
                        startValue: 90
                    },
                    {
                        alpha: 0.7,
                        color: "black",
                        endValue: 220,
                        id: "GaugeBand-3",
                        innerRadius: "95%",
                        startValue: 130
                    }
                ]
            }
        ],
        allLabels: [],
        balloon: {},
        titles: []
};