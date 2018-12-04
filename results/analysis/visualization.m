clear all
close all

fileName = 'D:\Source\HD\CSE6140-Project\data\Philadelphia.tsp';

fid = fopen(fileName);
cols = textscan(fid, '%d%f%f', 'HeaderLines', 5);
fclose(fid);

x = cols{2};
y = cols{3};

N = size(x, 1);

scatter(x, y);

pathFile = 'D:\Source\HD\CSE6140-Project\results\analysis\paths\NYC.path';

fid = fopen(pathFile);
pathCols = textscan(fid, '%d', N, 'Delimiter', ',');



