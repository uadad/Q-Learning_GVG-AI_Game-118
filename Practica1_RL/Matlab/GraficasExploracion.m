clear, clc, close all

% Cargar el archivo CSV
data = readtable('winRatesVsExplorationFija2.csv');

% Extraer los datos de episodios, winRate y explorationRate
Iteraciones = data.Iteraciones;  % Columna de episodios
Ganados = data.Ganados;   % Columna de tasas de victoria
exp = data.Exploracion; % Columna de tasa de exploración

% Graficar la evolución del win rate y explorationRate
figure;
yyaxis left; 
plot(Iteraciones, Ganados,'Color', 'k', 'LineWidth', 2, 'DisplayName', 'Nº Victorias');
ylabel('Nº Victorias', 'Color', 'k');
set(gca, 'YColor', 'k');

yyaxis right; % Usar el eje derecho para explorationRate
plot(Iteraciones, exp,'Color', 'g', 'LineWidth', 2, 'DisplayName', 'Valor exploracion');
ylabel('Exploracion', 'Color', 'g');
set(gca, 'YColor', 'g');

% Configurar el gráfico
grid on;
xlabel('Iteracion');
title('Exploracion-Explotacion Fija');

% Añadir leyenda
legend show;


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


