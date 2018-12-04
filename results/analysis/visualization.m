clear all
close all

cityname = 'NYC';
interval = 0.1; % in s

fileName = ['..\..\data\' cityname '.tsp'];

fid = fopen(fileName);
cols = textscan(fid, '%d%f%f', 'HeaderLines', 5);
fclose(fid);

x = cols{2};
y = cols{3};

N = size(x, 1);

pathFile = ['.\paths\' cityname '.path'];

fid = fopen(pathFile);
pathCols = textscan(fid, repmat('%d',1,N), 'Delimiter', ',');

paths = cell2mat(pathCols);

figure;
axis([min(x(:)) max(x(:)) min(y(:)) max(y(:))]);

for i = 1 : size(paths, 1)
    scatter(x, y);
    hold on;
    pathX = [x(paths(i,:)); x(paths(i,1))];
    pathY = [y(paths(i,:)); y(paths(i,1))];
    plot(pathX, pathY, '-');
    pause(.1);
    hold off;
end