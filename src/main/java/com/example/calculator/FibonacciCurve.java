package com.example.calculator;

import java.util.ArrayList;
import java.util.List;

public class FibonacciCurve {

    
    public static String generateFibonacciSVG(int n) {
        
        if (n <= 0) {
            return "<p>Please enter a positive integer for n.</p>";
        }

        
        final int width = 450;
        final int height = 450;
        final double cx = width / 2.0;
        final double cy = height / 2.0;
        final double margin = 40.0; 

        
        List<Double> fib = new ArrayList<>();
        fib.add(1.0);
        if (n > 1) fib.add(1.0);
        for (int i = 2; i < n; i++) {
            fib.add(fib.get(i - 1) + fib.get(i - 2));
        }

        double maxFib = fib.get(fib.size() - 1);
        double maxRadiusAllowed = Math.min(width, height) / 2.0 - margin;
        double baseScale = maxRadiusAllowed / Math.max(1.0, maxFib);

        final double phi = (1.0 + Math.sqrt(5.0)) / 2.0; 
        final double step = Math.PI / 180.0; 
        final double totalAngle = n * (Math.PI / 2.0);

        double r = fib.get(0) * baseScale;
        double theta = 0.0;
        double factorPerRad = Math.pow(phi, 1.0 / (Math.PI / 2.0));

  
        List<double[]> points = new ArrayList<>();

        
        for (double t = 0.0; t <= totalAngle + 1e-9; t += step) {
            if (t == 0.0) {
                r = fib.get(0) * baseScale;
            } else {
                r *= Math.pow(factorPerRad, step);
            }

            double x = cx + r * Math.cos(theta);
            double y = cy - r * Math.sin(theta);
            points.add(new double[]{x, y});

            theta += step;
        }

      
        double allowedRadius = Math.min(width, height) / 2.0 - margin;
        double maxDist = 0.0;
        for (double[] p : points) {
            double dx = p[0] - cx;
            double dy = p[1] - cy;
            double dist = Math.hypot(dx, dy);
            if (dist > maxDist) maxDist = dist;
        }
        double scaleToFit = 1.0;
        if (maxDist > allowedRadius) {
            scaleToFit = allowedRadius / maxDist;
        }

    
        StringBuilder path = new StringBuilder();
        for (int i = 0; i < points.size(); i++) {
            double[] p = points.get(i);
            double rx = p[0] - cx;
            double ry = p[1] - cy;
            double sx = cx + rx * scaleToFit;
            double sy = cy + ry * scaleToFit;
            if (i == 0)
                path.append("M ").append(format(sx)).append(" ").append(format(sy));
            else
                path.append(" L ").append(format(sx)).append(" ").append(format(sy));
        }

        StringBuilder svg = new StringBuilder();
        svg.append("<svg xmlns='http://www.w3.org/2000/svg' ")
                .append("width='").append(width).append("' height='").append(height).append("' ")
                .append("viewBox='0 0 ").append(width).append(" ").append(height).append("'>");

     
        svg.append("<rect width='100%' height='100%' fill='white'/>");

      
        svg.append("<text x='85%' y='58%' text-anchor='middle' font-size='13' font-weight='bold' fill='black'>X-axis</text>");

        svg.append("<text x='2%' y='50%' text-anchor='middle' font-size='13' font-weight='bold' fill='black' transform='rotate(-90, 15, 50)'>Y-axis</text>");

        svg.append("<line x1='0' y1='").append(cy).append("' x2='").append(width)
                .append("' y2='").append(cy).append("' stroke='#222' stroke-width='2' opacity='0.9'/>");
        svg.append("<line x1='").append(cx).append("' y1='0' x2='").append(cx)
                .append("' y2='").append(height).append("' stroke='#222' stroke-width='2' opacity='0.9'/>");

        
        svg.append("<g stroke='#eee' stroke-width='1'>");
        final int gridStep = 50;
        for (int gx = gridStep; gx < width; gx += gridStep) {
            svg.append("<line x1='").append(gx).append("' y1='0' x2='").append(gx)
                    .append("' y2='").append(height).append("'/>");
        }
        for (int gy = gridStep; gy < height; gy += gridStep) {
            svg.append("<line x1='0' y1='").append(gy).append("' x2='").append(width)
                    .append("' y2='").append(gy).append("'/>");
        }
        svg.append("</g>");

    
        svg.append("<path d='").append(path).append("' ")
                .append("stroke='#007BFF' stroke-width='3' fill='none' stroke-linecap='round' stroke-linejoin='round'/>");


        svg.append("</svg>");

        return svg.toString();
    }

    private static String format(double v) {
        return String.format("%.3f", v);
    }
}
