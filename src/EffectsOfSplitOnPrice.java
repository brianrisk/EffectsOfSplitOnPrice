import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Our main class that 
 * @author brianrisk
 *
 */
public class EffectsOfSplitOnPrice {

	static File stockDir = new File("data/stocks");
	static int imageWidth = 500;
	static int imageHeight = 500;
	
	// data charted
	static final int PRICE = 1;
	static final int VOLUME = 2;
	
	// event types
	static final int SPLIT = 1;
	static final int DIVIDEND = 2;
	static final int RANDOM = 3;
	
	static ArrayList<String> codes;
	static ArrayList<Stock> stocks;

	
	public static void main(String [] args) {
		
		// load codes and stocks
		File componentsFile = new File("data/components/components.txt");
		codes = DownloadStockData.loadQuandlCodes(componentsFile);
		Collections.shuffle(codes);
		stocks = new ArrayList<Stock>();
		for (String code: codes) {
			File stockFile = new File("data/stocks/" + code + ".csv");
			if (!stockFile.exists()) continue;
			stocks.add(new Stock(code));
		}
		
		// create the charts
		drawChart(PRICE, 2, 10, 10, 180, SPLIT, "price-split");
		drawChart(PRICE, 2, 10, 10, 180, DIVIDEND, "price-dividend");
		drawChart(PRICE, 2, 10, 10, 180, RANDOM, "price-random");
		drawChart(VOLUME, .04, 1, 10, 30, SPLIT, "volume-split");
		drawChart(VOLUME, .04, 1, 10, 30, DIVIDEND, "volume-dividend");
		drawChart(VOLUME, .04, 1, 10, 30, RANDOM, "volume-random");
		
		// "I'm finished!"
		U.p("done");
	}
	
	
	/**
	 * function to draw a chart given a data type (PRICE or VOLUME)
	 * and event (SPLIT, DIVIDEND, or RANDOM)
	 * 
	 * @param type
	 * @param scale
	 * @param ySubdivisions
	 * @param windowRadius
	 * @param event
	 * @param nameString
	 */
	public static void drawChart(
			int type, 
			double scale, 
			int ySubdivisions,
			int xGroup,
			int windowRadius, 
			int event, 
			String nameString) {
		
		// our random object if we are randomly selecting events
		Random random = new Random();
		
		// create graphics object
		BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bufferedImage.createGraphics();
		g.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
		g.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		g.setColor(Color.black);
		g.fillRect(0,0,imageWidth,imageHeight);
		
		// The pixel width of one day
		double xInc = (double) imageWidth / (windowRadius * 2 + 1);

		double [] yAverages = new double[windowRadius * 2 + 2];
		int yCount = 0;

		// loop through stocks
		// loop through days of stocks
		// if a stock day has a split and there are +- 30 days, draw it
		for (int stockIndex = 0; stockIndex < stocks.size(); stockIndex++) {
			Stock stock = stocks.get(stockIndex);
			ArrayList<StockDay> stockDays = stock.days;
			if (stockDays.size() < 2 * windowRadius + 1) continue;

			for (int dayIndex = windowRadius + 1; dayIndex < stockDays.size() - windowRadius - 1; dayIndex++) {
				StockDay day = stockDays.get(dayIndex);

				// based on our selected event, see if we are examining this particular day
				boolean eventIsTrue = false;
				if (event == SPLIT && day.split > 1) eventIsTrue = true;
				if (event == DIVIDEND && day.dividend > 0) eventIsTrue = true;
				if (event == RANDOM && random.nextDouble() < .004) eventIsTrue = true;
				
				if (eventIsTrue) {
					Point2D.Double [] points = new Point2D.Double [windowRadius * 2 + 2];
					double [] proportions = new double[windowRadius * 2 + 2];
					boolean skip = false;
					for (int subIndex = dayIndex - windowRadius - 1; subIndex < dayIndex + windowRadius + 1; subIndex ++) {
						StockDay subDay = stockDays.get(subIndex);
						
						// our index for the data arrays we are building
						int arrayIndex = subIndex - (dayIndex - windowRadius - 1);
						
						double xLoc = Math.round(xInc * (subIndex - (dayIndex - windowRadius - 1)));

						double proportion = 0;
						
						if (type == PRICE) {
							if  (day.adj_close < subDay.adj_close) {
								proportion = (subDay.adj_close - day.adj_close) / day.adj_close;
							} else {
								proportion = -1 * (day.adj_close - subDay.adj_close) / subDay.adj_close;
							}
							// catch for bad data
							if (Math.abs(proportion) > .75) skip = true;
						}
						if (type == VOLUME) {
							if  (day.adj_volume < subDay.adj_volume) {
								proportion = (subDay.adj_volume - day.adj_volume) / day.adj_volume;
							} else {
								proportion = -1 * (day.adj_volume - subDay.adj_volume) / subDay.adj_volume;
							}
							// catch for bad data
							if (subDay.adj_volume == 0 || day.adj_volume == 0) skip = true;
						}
						
						proportions[arrayIndex] = proportion;

						double yLoc = (imageHeight / 2) - Math.round((imageHeight / 2) * proportion * scale);

						points[arrayIndex] = new Point2D.Double(xLoc, yLoc);
					}

					// if we determined this data isn't suitable, skip it
					if (skip) continue;

					// keeping track of what the average y value will be
					yCount++;
					for (int pointIndex = 0; pointIndex < points.length; pointIndex++) {
						yAverages[pointIndex] += points[pointIndex].getY();
					}

					// setting color of stock line based on if it ends on a high or low value
					float proportion = (float) proportions[proportions.length - 1];
					proportion += .5f;
					if (proportion > 1) proportion = 1;
					if (proportion < .1) proportion = .1f;
					Color hue = Color.getHSBColor(proportion, 1, 1);
					Color stockColor = new Color(hue.getRed(), hue.getGreen(), hue.getBlue(), 64);
					g.setColor(stockColor);
					
					// drawing the stock line
					for (int pointIndex = 0; pointIndex < points.length - 1; pointIndex++) {
						Point2D.Double pointA = points[pointIndex];
						Point2D.Double pointB = points[pointIndex + 1];
						g.drawLine((int) pointA.getX(), (int) pointA.getY(), (int) pointB.getX() - 1, (int) pointB.getY());
					}
				}

			}

		}

		// dividing all y totals to get average
		for (int pointIndex = 0; pointIndex < yAverages.length; pointIndex++) {
			yAverages[pointIndex] /= yCount;
		}

		// drawing average line
		g.setColor(Color.WHITE);
		g.setStroke(new BasicStroke(2.0f));
		for (int pointIndex = 0; pointIndex < yAverages.length - 1; pointIndex++) {
			int xA = (int) Math.round(xInc * pointIndex);
			int xB = (int) Math.round(xInc * (pointIndex + 1));
			int yA = (int) Math.round(yAverages[pointIndex]);
			int yB = (int) Math.round(yAverages[pointIndex + 1]);
			g.drawLine(xA, yA, xB, yB);
		}

		// drawing x axis
		g.setStroke(new BasicStroke(1.0f));
		g.setColor(Color.WHITE);
		g.drawLine(0, imageHeight / 2, imageWidth, imageHeight / 2);
		double xTic = xInc ;
		while (xTic < imageWidth) {
			g.drawLine((int) xTic, imageHeight / 2 + 1, (int) xTic, imageHeight / 2 + 3);
			xTic += (xInc * xGroup);
		}
		
		// drawing y axis
		int mid = (int) ((windowRadius + 1) * xInc);
		g.drawLine(mid, 0, mid, imageHeight);
		
		//drawing y tics and labels
		g.setFont(new Font("Monospaced", Font.PLAIN, 10));
		FontMetrics fm = g.getFontMetrics();
		int yLoc = imageHeight / 2;
		int i = 1;
		while (yLoc > 0) {
			double proportion = (double) i / ySubdivisions;
			yLoc = (int) ((imageHeight / 2) - Math.round((imageHeight/2) * proportion * scale));
			g.drawLine(mid + 1, yLoc, mid + 3, yLoc);
			g.drawString(proportion + "", mid + 5, yLoc);
			i++;
		}
		yLoc = imageHeight / 2;
		i = -1;
		while (yLoc < imageHeight) {
			double proportion = (double) i / ySubdivisions;
			yLoc = (int) ((imageHeight / 2) - Math.round((imageHeight/2) * proportion * scale));
			g.drawLine(mid + 1, yLoc, mid + 3, yLoc);
			g.drawString(proportion + "", mid + 5, yLoc);
			i--;
		}

		// saving graphic object as PNG
		try {
			ImageIO.write(bufferedImage,"PNG",new File("output/" + nameString + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}





}