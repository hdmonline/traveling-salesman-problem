clear all
close all
clc
set(0, 'DefaultAxesFontWeight', 'bold', ...
    'DefaultAxesFontSize', 30, ...
    'DefaultAxesFontName', 'Times',...
    'DefaultAxesTitleFontWeight', 'bold');

properties = {'PlotBoxAspectRatio', [4 3 3], 'LineStyleOrder', {'-', '--', ':', '-.'},...
    'LineWidth', 2.0, 'Box', 'on', 'color', 'none',...
    'XGrid', 'on', 'YGrid', 'on', 'ZGrid', 'on'...
    'XMinorGrid', 'off', 'YMinorGrid', 'off',...
    'XMinorTick', 'on', 'YMinorTick', 'on'};     
folder = '.\Champaign_LS2_100\';
groundTruth = 52643;
thresholds = [0.15, 0.2, 0.25, 0.30, 0.35];
cutoff_times = [0.2, 0.4, 0.8, 1.5, 3];


addpath(folder);
fileList = struct2cell(dir([folder, '*.trace']));
fileList = fileList(1,:);

quality = zeros(length(fileList), length(cutoff_times));
x = cell(length(cutoff_times)); y = cell(length(cutoff_times));

actual_fig = fullfig(); 
actual_ax = gca; 
% set(actual_ax, 'Color', 'none', 'LineWidth', 2.0, 'Box', 'on', 'YGrid', 'on', 'XGrid', 'on');
set(actual_ax, properties{:});
hold on;
xlabel(actual_ax, 'Relative Solution Quality (%)'); 
ylabel('P(solve)');


for i = 1:length(fileList)
    data = importdata(fileList{i});
    time = data(:,1); distance = data(:,2);
    quality(i,:) = get_quality(data, cutoff_times, groundTruth);
end

for j = 1:length(cutoff_times)
    garbage_fig = figure();
    [h, stats] = cdfplot(quality(:,j));
    x{j} = h.XData(2:end-1); 
    y{j} = h.YData(2:end-1); 
    close(garbage_fig);        
    [x_u, index] = unique(x{j});
    X = linspace(min(x{j}), max(x{j}), 1000); 
    Y = interp1(x_u, y{j}(index), X);
    plot(actual_ax, X, Y, 'LineWidth', 2.0)
end     

legend('0.2s','0.4s','0.8s','1.5s','3s');