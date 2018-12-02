clear all
close all

folder = '.\Berlin100\';
groundTruth = 7542;
thresholds = [0.15, 0.2, 0.25];
x = 0.1:0.1:120;

addpath(folder);
fileList = dir([folder, '*.trace']);
numFiles = numel(fileList);
numTh = size(thresholds, 2);

runtimes = zeros(numTh, numFiles);
success = zeros(numTh, numFiles, size(x,2));
for iFile = 1 : numFiles
    fileName = fileList(iFile).name;
    fid = fopen([folder fileName]);
    cols = textscan(fid, '%f%d', 'Delimiter', ',');
    times = cols{1};
    distances = cols{2};
    fclose(fid);
    
    currPSolves = zeros(numTh, 1, size(x, 2));
    currRuntimes = zeros(numTh, 1);
    for jTh = 1 : numTh
        desiredDist = groundTruth * (1 + thresholds(jTh));
        if size(find(distances <= desiredDist, 1), 1) == 0
            disp([fileName 'has not reach desired result for threshold = ' num2str(thresholds(jTh))]);
        else
            currRuntimes(jTh, 1) = times(find(distances <= desiredDist, 1));
        end
        
        % real-time distribution
        isSuccess = false(1,1,size(x, 2));
        for i = 1 : size(x, 2)
            if size(find(times <= x(i), 1, 'last'), 2) ~= 0
                isSuccess(1,1,i) = distances(find(times <= x(i), 1, 'last')) <= desiredDist;
            end
        end
        success(jTh, iFile, :) = isSuccess;
    end
    runtimes(:, iFile) = currRuntimes;
end

%% bar chart 
for iTh = 1 : numTh
    figure;
    bar(runtimes(iTh, :));   
end

%% plot real-time distribution
pSolves = sum(success, 2);
% reduce dimension
pSolves = squeeze(pSolves) ./ 100;
figure;
hold on;
set(gca, 'XScale', 'log');
for iTh = 1 : numTh
    semilogx(x, pSolves(iTh, :));
end
hold off;
