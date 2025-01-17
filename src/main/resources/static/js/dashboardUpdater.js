async function fetchServerData(url, element, dataMapper, classMapper) {
    try {
        const response = await fetch(url);
        if (!response.ok) throw new Error("Network response was not ok.");
        const data = await response.json();

        element.innerHTML = dataMapper(data);

        element.classList = [];
        element.classList.add(classMapper(data));
    } catch (error) {
        console.error("Error updating server status: ", error);
    }
}

function updateServerStatus(serverId, element) {
    const statusMap = {
        "UP": "Server Up",
        "DOWN": "Server Down",
        "UNKNOWN": "Unknown"
    };

    const classMap = {
        "UP": "server-status-online",
        "DOWN": "server-status-offline",
        "UNKNOWN": "server-status-unknown"
    };

    fetchServerData(`/api/internal/servers/get/status/${serverId}`, element,
        (status) => statusMap[status] || "Unknown",
        (status) => classMap[status] || "server-status-unknown"
    );
}

function updateServerPing(serverId, element) {
    fetchServerData(`/api/internal/servers/ping/${serverId}`, element,
        (ping) => ping === -1 ? "Ping: Timed Out" : `Ping: ${ping} ms`,
        ""
    );
}

document.querySelectorAll("div#server-status").forEach(element => {
    const serverId = element.getAttribute("data-server-update-id");
    setInterval(() => updateServerStatus(serverId, element), 10000);
});

document.querySelectorAll("div#server-ping").forEach(element => {
    const serverId = element.getAttribute("data-server-ping-id");
    setInterval(() => updateServerPing(serverId, element), 10000);
});
