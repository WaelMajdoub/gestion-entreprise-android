package com.gestionentreprise.gui.chart;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gestionentreprise.R;
import com.gestionentreprise.gui.activity.GeneralActivity;
import com.gestionentreprise.gui.activity.SuiviActivity;
import com.gestionentreprise.utils.ChartUtils;

public class Chart {
    /** The chart view that displays the data. */
    private GraphicalView graphView;
    /** The main dataset that includes all the series that go into a chart. */
    private XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    /** The main renderer that includes all the renderers customizing a chart. */
    private XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

    private SuiviActivity context;

    public Chart(SuiviActivity context) {
	this.context = context;
    }

    public void onCreateAction() {

	int[] colors = new int[] { 0xFF000033, 0xFF3333FF, 0xFF336633 };
	PointStyle[] styles = new PointStyle[] { PointStyle.DIAMOND, PointStyle.DIAMOND, PointStyle.DIAMOND };

	renderer = ChartUtils.buildRenderer(colors, styles);

	String[] titles = new String[] { "CA", "CA - charges (RSI)", "Salaire Net" };
	List<double[]> x = new ArrayList<double[]>();
	for (int i = 0; i < titles.length; i++) {
	    x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });
	}
	List<double[]> values = new ArrayList<double[]>();
	values.add(new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
	values.add(new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
	values.add(new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
	dataset = ChartUtils.buildDataset(titles, x, values);
    }

    public void setData(String[] titles, double[] x, List<double[]> values) {
	List<double[]> listX = new ArrayList<double[]>();
	for (int i = 0; i < titles.length; i++) {
	    listX.add(x);
	}
	dataset.clear();
	ChartUtils.addXYSeries(dataset, titles, listX, values, 0);

	graphView.repaint();
    }

    public void setData(String[] titles, List<String> x, List<double[]> values) {
	dataset.clear();
	ChartUtils.addXYSeries(dataset, renderer, titles, x, values, 0);

	graphView.repaint();
    }

    public void onResumeAction() {
	if (graphView == null) {
	    LinearLayout layout = (LinearLayout) context.findViewById(R.id.chart);
	    graphView = ChartFactory.getLineChartView(context, dataset, renderer);
	    // enable the chart click events
	    renderer.setClickEnabled(true);
	    renderer.setSelectableBuffer(10);
	    graphView.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		    SeriesSelection seriesSelection = graphView.getCurrentSeriesAndPoint();

		    if (seriesSelection != null) {
			Toast.makeText(context,
			               GeneralActivity.df.format(seriesSelection.getValue()) + "€",
			               Toast.LENGTH_SHORT).show();
		    }
		}
	    });

	    layout.addView(graphView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

	} else {
	    graphView.repaint();
	}
    }
}
