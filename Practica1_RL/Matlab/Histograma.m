% Leer archivo Excel
filename = 'Estados.csv'; % Cambiar al nombre de tu archivo
data = readtable(filename, 'TextType', 'string'); 

% Convertir los estados a cadenas concatenadas
estados = join(data{:, :}, ',');

% Contar las ocurrencias de cada estado único
[uniqueEstados, ~, idx] = unique(estados); 
conteo = accumarray(idx, 1);

% Generar identificadores únicos para cada estado
identificadores = "Estado" + (1:length(uniqueEstados))';

% Crear una tabla que asocia identificadores, estados y frecuencias
resultados = table(identificadores, uniqueEstados, conteo, ...
    'VariableNames', {'Identificador', 'Estado', 'NºVeces'});

% Mostrar los resultados
disp('Estados únicos con identificadores y su conteo:');
disp(resultados);

figure;
bar(conteo);
title('Histograma Estados');
ylabel('NºVeces');
grid on;

xticks(1:length(identificadores));
xticklabels(identificadores);
