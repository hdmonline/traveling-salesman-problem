function quality = get_quality(trace_data, cutoff_time, ground_truth)
% Returns an n x 1 vector of relative errors where n = number of
% cutoff_times
    if (nargin < 3)
        ground_truth = 7542;
    end
    time = trace_data(:,1); 
    distance = trace_data(:,2);
    quality = zeros(1,length(cutoff_time));
    for i = 1:length(cutoff_time)
        indices = time <= cutoff_time(i);
        distance_cutoff = distance(indices); % contains all distances from time = 0 to time = cutoff_time
        quality(i) = 100.*(distance_cutoff(end) - ground_truth)./ground_truth;
    end
end