rm /data/flow.json

# Vai alla directory di lavoro di Node-RED
cd /usr/src/node-red

# Esegui l'installazione del pacchetto node-red-dashboard
npm install node-red-dashboard

# Riavvia il container di Node-RED
docker restart node-red