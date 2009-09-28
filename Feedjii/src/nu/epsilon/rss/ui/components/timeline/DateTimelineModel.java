/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.ui.components.timeline;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author j
 */
public class DateTimelineModel implements TimelineModel<Date> {

    private Date start;
    private Date end;
    private final static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public DateTimelineModel(Date start, Date end) {
        if (start.after(end)) {
            this.start = end;
            this.end = start;
        } else {
            this.start = start;
            this.end = end;
        }
    }

    public Date getMin() {
        return start;
    }

    public Date getMax() {
        return end;
    }

    public double getPercentage(Date value) {
        if (!value.after(start)) {
            return 0D;
        }

        if (!value.before(end)) {
            return 1D;
        }

        long length = end.getTime() - start.getTime();
        return ((double) (value.getTime() - start.getTime())) / length;
    }

    @SuppressWarnings("deprecation")
    public Date getValueAt(double percent) {
        long length = end.getTime() - start.getTime();

        //The "exact" point on the scale
        long stamp = Math.round(((double) length) * percent);
        Date result = new Date(stamp + start.getTime());

        //Remove time
        result.setHours(0);
        result.setMinutes(0);
        result.setSeconds(0);

        return result;
    }

    public List<String> getLables(int maxNumber) {
        List<String> labels = new LinkedList<String>();

        int num = 7;
        labels.add(formatter.format(getMin()));
        for (int i = 1; i < num - 1; i++) {
            double percent = ((double) i) / (num - 1);
            Date value = getValueAt(percent);
            labels.add(formatter.format(value));
        }
        labels.add(formatter.format(getMax()));

        return labels;
    }
}
