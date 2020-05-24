package main;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;


public class Plot extends ApplicationFrame {
    public Plot(final String title, long []results) {
        super(title);
        final XYSeries recSeries = new XYSeries("time");
        for (int i = 0; i < results.length; i++) {
            recSeries.add(i, results[i]);
        }
        final XYSeriesCollection data = new XYSeriesCollection(recSeries);
        final JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "pack size",
                "time[µs]",
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1200, 600));
        setContentPane(chartPanel);
    }
}
