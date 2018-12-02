clear all
close all

folder = '.\Berlin100\';
groundTruth = 7542;
thresholds = [0.15, 0.2, 0.25];

addpath(folder);
fileList = dir([folder, '*.trace']);
numFiles = numel(fileList);
numTh = size(thresholds, 2);

runtimes = zeros(numTh, numFiles);
success = 0;
for iFile = 1 : numFiles
    fileName = fileList(iFile).name;
    fid = fopen([folder fileName]);
    cols = textscan(fid, '%f%d', 'Delimiter', ',');
    times = cols{1};
    distances = cols{2};
    fclose(fid);
    
    currRuntimes = zeros(numTh, 1);
    for jTh = 1 : numTh
        desiredDist = groundTruth * (1 + thresholds(jTh));
        if size(find(distances <= desiredDist, 1), 1) == 0
            disp([fileName 'has not reach desired result for threshold = ' num2str(thresholds(jTh))]);
        else
            currRuntimes(jTh, 1) = times(find(distances <= desiredDist, 1));
        end
    end
    runtimes(:, iFile) = currRuntimes;
end

for iTh = 1 : numTh
    % bar chart 
    figure;
    bar(runtimes(iTh, :));
    
    
end

