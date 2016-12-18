package com.gestionentreprise.utils;

import java.util.List;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Color;
import android.graphics.Paint.Align;

public class ChartUtils {
    public static XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
	XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	setRenderer(renderer, colors, styles);
	int length = renderer.getSeriesRendererCount();
	for (int i = 0; i < length; i++) {
	    ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
	}
	setChartSettings(renderer, "", "Mois", "", 0.5, 13.5, 0, 2400, Color.BLACK, Color.BLACK);
	renderer.setXLabels(13);
	renderer.setYLabels(10);
	renderer.setShowGrid(true);
	renderer.setXLabelsAlign(Align.RIGHT);
	renderer.setYLabelsAlign(Align.RIGHT);
	renderer.setZoomButtonsVisible(true);
	renderer.setXLabelsColor(Color.DKGRAY);
	renderer.setYLabelsColor(0, Color.DKGRAY);
	renderer.setApplyBackgroundColor(true);
	renderer.setBackgroundColor(Color.WHITE);
	renderer.setMarginsColor(Color.WHITE);
	// renderer.setPanLimits(new double[] { 0, 2000, 0, 2000 });
	// renderer.setZoomLimits(new double[] { 0, 2000, 0, 2000 });
	return renderer;
    }

    public static XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues, List<double[]> yValues) {
	XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	addXYSeries(dataset, titles, xValues, yValues, 0);
	return dataset;
    }

    public static void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues,
	    List<double[]> yValues, int scale) {
	int length = titles.length;
	for (int i = 0; i < length; i++) {
	    XYSeries series = new XYSeries(titles[i], scale);
	    double[] xV = xValues.get(i);
	    double[] yV = yValues.get(i);
	    int seriesLength = xV.length;
	    for (int k = 0; k < seriesLength; k++) {
		series.add(xV[k], yV[k]);
	    }
	    dataset.addSeries(series);
	}
    }

    public static void addXYSeries(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer, String[] titles,
	    List<String> xValues, List<double[]> yValues, int scale) {
	int length = titles.length;
	int lengthX = xValues.size();
	double[] xV = new double[lengthX];
	for (int i = 0; i < lengthX; i++) {
	    xV[i] = i + 1;
	}

	for (int i = 0; i < length; i++) {
	    XYSeries series = new XYSeries(titles[i], scale);
	    double[] yV = yValues.get(i);
	    int seriesLength = xV.length;
	    for (int k = 0; k < seriesLength; k++) {
		series.add(xV[k], yV[k]);
	    }
	    dataset.addSeries(series);
	}

	for (int i = 0; i < xValues.size(); i++) {
	    renderer.addXTextLabel(i + 1, xValues.get(i));
	}
	renderer.setXAxisMax(lengthX + 0.5);
	renderer.setXLabelsAlign(Align.CENTER);
	renderer.setXLabels(0);
    }

    private static void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
	renderer.setAxisTitleTextSize(16);
	renderer.setChartTitleTextSize(20);
	renderer.setLabelsTextSize(15);
	renderer.setLegendTextSize(15);
	renderer.setPointSize(5f);
	renderer.setMargins(new int[] { 20, 40, 15, 20 });
	int length = colors.length;
	for (int i = 0; i < length; i++) {
	    XYSeriesRenderer r = new XYSeriesRenderer();
	    r.setColor(colors[i]);
	    r.setPointStyle(styles[i]);
	    renderer.addSeriesRenderer(r);
	}
    }

    private static void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle, String yTitle,
	    double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) {
	renderer.setChartTitle(title);
	renderer.setXTitle(xTitle);
	renderer.setYTitle(yTitle);
	renderer.setXAxisMin(xMin);
	renderer.setXAxisMax(xMax);
	renderer.setYAxisMin(yMin);
	renderer.setYAxisMax(yMax);
	renderer.setAxesColor(axesColor);
	renderer.setLabelsColor(labelsColor);
    }

}
