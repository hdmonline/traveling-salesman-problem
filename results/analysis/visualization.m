clear all
close all

cityname = 'Roanoke';
algo = 'LS2'; % BnB | Approx | LS1 | LS2
interval = 0.1; % in s

fileName = ['..\..\data\' cityname '.tsp'];

fid = fopen(fileName);
cols = textscan(fid, '%d%f%f', 'HeaderLines', 5);
fclose(fid);

x = cols{2};
y = cols{3};

N = size(x, 1);

label = cellstr(num2str([1:N]'));

pathFile = ['.\paths\' cityname '_' algo '.path'];

fid = fopen(pathFile);
pathCols = textscan(fid, repmat('%d',1,N), 'Delimiter', ',');
fclose(fid);

paths = cell2mat(pathCols);

figure;
axis([min(x(:)) max(x(:)) min(y(:)) max(y(:))]);

for i = 1 : size(paths, 1)
    scatter(x, y);
    text(x,y,label);
    hold on;
    pathX = [x(paths(i,:)); x(paths(i,1))];
    pathY = [y(paths(i,:)); y(paths(i,1))];
    plot(pathX, pathY, '-');
    pause(.1);
    hold off;
end