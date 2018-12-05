clear all
close all

properties = {'PlotBoxAspectRatio', [4 3 3], 'LineStyleOrder', {'-', '--', ':', '-.'},...
    'LineWidth', 2.0, 'Box', 'on', 'color', 'none',...
    'XGrid', 'on', 'YGrid', 'on', 'ZGrid', 'on'...
    'XMinorGrid', 'off', 'YMinorGrid', 'off'};  

folder = '.\Berlin_LS2_100\';
groundTruth = 7542;
thresholds = [0.18, 0.2, 0.25, 0.30, 0.35];
%cutofftimes = [5, 10, 20, 50, 100];
x = 0.1:0.02:120;
%x2 = 0.1:0.1:35;

addpath(folder);
fileList = dir([folder, '*.trace']);
numFiles = numel(fileList);
numTh = size(thresholds, 2);
%numCutT = size(cutofftimes, 2);
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
    semilogx(x, pSolves(iTh, :), 'LineWidth', 2.0);
end
set(gca, properties{:})
xlabel('Runtime (seconds)')
ylabel('P(solve)');
legend('18%','20%','25%','30%','35%');
xlim([1e-2, 1e2])
%set(gca, 'XTick', [1e-1, 1e0, 1e1, 1e2, 1e3])
set(gca, 'XTick', [1e-2,1e-1, 1e0, 1e1, 1e2])
hold off;

