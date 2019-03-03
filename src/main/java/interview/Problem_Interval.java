package interview;

import java.util.*;

public class Problem_Interval {
}


class L759_Employee_Free_Time {

    public class Interval {
        int start;
        int end;
        Interval() { start = 0; end = 0; }
        Interval(int s, int e) { start = s; end = e; }
    }

    public List<Interval> employeeFreeTime_LoadOneByOne(List<List<Interval>> schedule) {
        TreeSet<Interval> tree = new TreeSet<>(new Comparator<Interval>(){
            @Override
            public int compare(Interval i1, Interval i2) {
                return i1.start - i2.start;
            }
        });

        for(List<Interval> intervals : schedule) {
            for(Interval interval : intervals) {
                add(tree, interval);
            }
        }

        //题目中没有整体的时间起始位置，按照提供的时间列表中最小和最大作为整体的时间窗口
        List<Interval> result = new ArrayList<>();
        int min = tree.first().start;
        while(!tree.isEmpty()) {
            Interval interval = tree.pollFirst();
            if(min < interval.start) {
                result.add(new Interval(min, interval.start));
            }
            min = interval.end;
        }

        return result;
    }

    void add(TreeSet<Interval> tree, Interval interval) {
        Interval prev = tree.floor(interval);
        Interval next = tree.ceiling(interval);

        // tree has an existing node which has the
        // same start with the current interval
        if(prev!= null && next != null && prev == next) {
            interval.end = Math.max(prev.end, interval.end);
            tree.remove(prev);
            prev = tree.floor(interval);
            next = tree.ceiling(interval);
        }

        // while next is included in current interval, remove it
        while(next != null && next.end <= interval.end) {
            tree.remove(next);
            next = tree.ceiling(interval);
        }

        if(prev == null && next == null) {
            tree.add(interval);
        } else if(prev == null) {
            if(interval.end < next.start) {
                tree.add(interval);
            } else {
                next.start = interval.start;
            }
        } else if(next == null) {
            if(interval.start > prev.end) {
                tree.add(interval);
            } else if(interval.end > prev.end) {
                prev.end = interval.end;
            }
            //ignore when current interval is included in prev interval
        } else {
            int gapStart = prev.end;
            int gapEnd = next.start;
            if(interval.start > gapStart && interval.end < gapEnd) {
                tree.add(interval);
            } else if(interval.start <= gapStart && interval.end >= gapEnd) {
                interval.start = prev.start;
                interval.end = next.end;
                tree.remove(prev);
                tree.remove(next);
                tree.add(interval);
            } else if(interval.start <= gapStart) {
                prev.end = Math.max(prev.end, interval.end);    // bug1: current interval may be included in prev interval
            } else if(interval.end >= gapEnd) {
                next.start = interval.start;
            }
        }
    }

    void dump(TreeSet tree) {
        Iterator<Interval> p = tree.iterator();
        while(p.hasNext()) {
            Interval i = p.next();
            System.out.print(String.format("[%d, %d], ", i.start, i.end));
        }
        System.out.println(" ");
    }

    public List<Interval> employeeFreeTime_LoadAll(List<List<Interval>> schedule) {
        List<Interval> allIntervals = new ArrayList<>();
        for(List<Interval> intervals : schedule) {
            for(Interval interval : intervals) {
                allIntervals.add(interval);
            }
        }

        Collections.sort(allIntervals, new Comparator<Interval>(){
            @Override
            public int compare(Interval i1, Interval i2) {
                return i1.start - i2.start;
            }
        });

        List<Interval> result = new ArrayList<>();
        Interval prev = null, first = allIntervals.get(0);
        int start = first.start;
        int end = first.end;

        for(Interval interval : allIntervals) {
            if(end >= interval.start) {
                end = Math.max(end, interval.end);
            } else {
                if(prev == null) {
                    prev = new Interval(start, end);
                    start = interval.start;
                    end = interval.end;
                } else {
                    result.add(new Interval(prev.end, start));
                    prev.start = start;
                    prev.end = end;
                    start = interval.start;
                    end = interval.end;
                }
            }
        }

        result.add(new Interval(prev.end, start));
        return result;
    }
}



class L56_Merge_Intervals {
    public class Interval {
        int start;
        int end;
        Interval(int s, int e) {
            start = s;
            end = e;
        }
    }

    public List<Interval> merge(List<Interval> intervals) {
        List<Interval> result = new ArrayList<>();
        if(intervals == null || intervals.size() == 0) {
            return result;
        }


        Comparator<Interval> comp = new Comparator<Interval>(){
            @Override
            public int compare(Interval i1, Interval i2) {
                return i1.start - i2.start;
            }
        };

        Collections.sort(intervals,comp);

        Interval first = intervals.get(0);
        int start = first.start, end = first.end;
        for(Interval interval : intervals) {
            if(interval.start <= end) {
                end = Math.max(end, interval.end);
            } else {
                result.add(new Interval(start, end));
                start = interval.start;
                end = interval.end;
            }
        }

        result.add(new Interval(start, end));
        return result;
    }
}


class L57_Insert_Interval {

    public class Interval {
        int start;
        int end;
        Interval(int s, int e) {
            start = s;
            end = e;
        }
    }
    public List<Interval> insert(List<Interval> intervals, Interval newInterval) {
        Comparator<Interval> comp = new Comparator<Interval>(){
            @Override
            public int compare(Interval i1, Interval i2) {
                return i1.start - i2.start;
            }
        };

        List<Interval> newIntervals = new ArrayList<>();
        newIntervals.addAll(intervals);
        newIntervals.add(newInterval);
        Collections.sort(newIntervals, comp);

        Interval first = newIntervals.get(0);
        int start = first.start;
        int end = first.end;

        List<Interval> result = new ArrayList<>();
        for(Interval interval : newIntervals) {
            if(interval.start <= end) {
                end = Math.max(end, interval.end);
            } else {
                result.add(new Interval(start, end));
                start = interval.start;
                end = interval.end;
            }
        }
        result.add(new Interval(start, end));
        return result;
    }
}




