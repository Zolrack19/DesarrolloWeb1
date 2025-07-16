export class PanelSolicitudes {
  #contenedorPrincipal
  #contextPath
  #getFechaData
  #cbxTipoBusqueda
  #graficoCanva1
  #graficoCanva2
  #graficoCanva3
  #dataSolicitudes
  #divItem


  constructor(contenedorPrincipal, getFechaData, contextPath) {
    this.#contenedorPrincipal = contenedorPrincipal
    this.#contextPath = contextPath
    this.#getFechaData = getFechaData
    this.htmlPanel = document.createElement("div")
    this.#dataSolicitudes = {}
    this.#configurarHtml()
  }

  #configurarHtml() {
    let nose = ""
    !JSON.parse(sessionStorage.getItem("usuario")).rolColaborador
    ? nose = ``
    : JSON.parse(sessionStorage.getItem("usuario")).rolColaborador === "Administrador"
    ? nose = `
      <div class="w-full md:w-40">
        <select id="tipoBusqueda"
          class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
          <option value="-1">Todas las solicitudes</option>
          <option value="1">Por colaborador</option>
          <option value="2">Por cliente</option>
        </select>
      </div>
      
      <div class="relative w-full">
        <input type="text" id="txtBuscar" placeholder="Buscar entidad"
          class="w-full md:flex-1 px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
        <ul id="popupResultados" tabindex="1"
          class="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-md max-h-40 overflow-y-auto hidden">
        </ul>
      </div>
    `
    : ""
    
    this.htmlPanel.className = "container mx-auto space-y-6"
    this.htmlPanel.innerHTML = `
      <h2 class="text-base font-medium text-gray-700 text-2xl mb-2">Criterios de búsqueda específica</h2>
      <div class="flex flex-col md:flex-row items-stretch md:items-center gap-7 mt-2 mb-4">
        ${nose}
      
        <button type="button"
          class="whitespace-nowrap w-full md:w-auto px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:ring-2 focus:ring-blue-300"
          id="btnVerEstadistica">
          Ver estadísticas
        </button>
      </div>

      <div class="bg-white rounded-xl shadow p-4">
        <canvas id="canva1" class="w-full h-100"></canvas>
      </div>
  
      <div class="flex gap-4">
        
        <div class="w-1/2 bg-white rounded-xl shadow p-4">
          <canvas id="canva2" class="w-full h-74"></canvas>
        </div>
        
        <div class="w-1/2 bg-white rounded-xl shadow p-4">
          <canvas id="canva3" class="w-full h-74"></canvas>
        </div>
  
      </div>
    `
    this.#cbxTipoBusqueda = this.htmlPanel.querySelector("select[id='tipoBusqueda']")
    
    this.#divItem = document.createElement("div")
    this.#divItem.className = "flex items-center justify-between w-full px-3 bg-gray-50 border border-gray-300 rounded-md shadow-sm"
    this.#divItem.innerHTML = `
    <li id="itemSeleccionado" class="list-none py-2 text-gray-800 font-medium"></li>
      <button id="btnItem" class="text-gray-600 hover:text-white hover:bg-red-500 bg-gray-200 rounded-full p-1 text-xl transition-all duration-200">
        &times;
      </button>
    </div>`
    this.#configurarBusqueda()
  }

  init() {
    this.#contenedorPrincipal.innerHTML = ""
    this.#contenedorPrincipal.appendChild(this.htmlPanel)
  }

  #configurarBusqueda() {
    let task
    let cbxValorUsado

    const item = this.#divItem.querySelector("li[id='itemSeleccionado']")
    const btnItem = this.#divItem.querySelector("button[id='btnItem']")
    btnItem.addEventListener("click", () => {
      this.#cbxTipoBusqueda.disabled = false
      item.removeAttribute("id")
      this.#divItem.replaceWith(txtBuscar)
    })
    
    const txtBuscar = this.htmlPanel.querySelector("input[id='txtBuscar']")
    const popupResultados = this.htmlPanel.querySelector("ul[id='popupResultados']")
    popupResultados.addEventListener('click', e => {
      if (e.target.tagName === 'LI') {
        item.innerHTML = e.target.innerHTML
        item.dataset.id = e.target.dataset.id

        this.#cbxTipoBusqueda.value = cbxValorUsado
        this.#cbxTipoBusqueda.disabled = true

        txtBuscar.replaceWith(this.#divItem)
        popupResultados.classList.add("hidden")
      }
    });
    popupResultados.addEventListener("focusout", () => {
      setTimeout(() => {
        if (!popupResultados.contains(document.activeElement)) {
          popupResultados.classList.add("hidden")
        }
      }, 0);
    });
    popupResultados.addEventListener("keypress", e => {
      if ((e.key === 'Enter' || e.key === ' ')) {
        e.preventDefault();
        item.innerHTML = document.activeElement.innerHTML
        item.dataset.id = document.activeElement.dataset.id
        
        this.#cbxTipoBusqueda.value = cbxValorUsado
        this.#cbxTipoBusqueda.disabled = true

        txtBuscar.replaceWith(this.#divItem)
        popupResultados.classList.add("hidden")
      }
    });

    txtBuscar.addEventListener("focusout", () => {
      setTimeout(() => {
        if (popupResultados !== document.activeElement.parentElement) {
          popupResultados.classList.add("hidden")
        }
      }, 100);
    })
    txtBuscar.addEventListener("focusin", () => {
      if (popupResultados.hasChildNodes()) {
        popupResultados.classList.remove("hidden")
      }
    })

    txtBuscar.addEventListener("input", e => {
      if (this.#cbxTipoBusqueda.value === "-1" || (this.#cbxTipoBusqueda.value !== "1" && this.#cbxTipoBusqueda.value !== "2")) return

      clearTimeout(task)
      task = setTimeout(async () => {
        const tipoBusqueda = this.#cbxTipoBusqueda.value
        const txtTokens = e.target.value.trim().replace(/\s+/g, ' ')
        if (!txtTokens || txtTokens.length === 0) return

        await fetch(`/${this.#contextPath}/control/${tipoBusqueda === "1"? "ColaboradorServlet?estricto=false&": "ClienteServlet?"}action=1&txtTokens=${encodeURIComponent(txtTokens)}`)
          .then(resp => {
            if (resp.ok) {
              return resp.json()
            }
          })
          .then(data => {
            if (data.ok) {
              popupResultados.innerHTML = ""
              let resultados
              if (tipoBusqueda === "1") {
                resultados = data.colaboradores
              } else {
                resultados = data.clientes
              }
              resultados.forEach(resultado => {
                const li = document.createElement("li");
                li.tabIndex = 0
                li.className = "px-4 py-2 hover:bg-blue-100 focus:bg-blue-200 focus:outline-none cursor-pointer";
                li.dataset.id = resultado.id
                li.innerHTML = tipoBusqueda === "1" ? `${resultado.nombre} ${resultado.apellidoPaterno} ${resultado.apellidoMaterno}` :
                resultado.razonSocial
                popupResultados.appendChild(li);
              });
              cbxValorUsado = tipoBusqueda
              popupResultados.classList.remove("hidden")
            } else {
              alert(data.error)
            }
          }) 
      }, 400);
    })

    this.#cbxTipoBusqueda.addEventListener("change", (e) => {
      switch (e.target.value) {
        case "-1":
          txtBuscar.disabled = true
          break;
        case "1":
        case "2":
          txtBuscar.disabled = false
          break;
        default:
          this.#cbxTipoBusqueda.value = "-1"
          break;
      }
    })

    const btnVerEstadistica = this.htmlPanel.querySelector("button[id='btnVerEstadistica']")
    btnVerEstadistica.addEventListener("click", async () => {
      let hayId
      if (this.htmlPanel.contains(txtBuscar)) {
        hayId = false
      } else if (item.dataset.id) {
        hayId = true
      } else {
        alert("debe seleccionar un usuario en específico")
        return;
      }

      const params = this.#getFechaData()
      params.action = 3
      if (hayId) {
        params.entidadId = item.dataset.id
        params.tipoBusqueda = cbxValorUsado
      } else {
        params.tipoBusqueda = -1
      }
      
      
      await fetch(`/${this.#contextPath}/control/SolicitudServlet?${new URLSearchParams(params)}`)
      .then(resp => {
        if (resp.ok) {
          return resp.json()
        }
      }).then(data => {
        if (data.ok) {
          this.#dataSolicitudes.solicitudes = data.solicitudes
          this.#procesarDatos()
          this.#dibujarGraficos(params.anio, params.mes)
        } else {
          alert(data.error)
        }
      })


    })    

  }
  
  #procesarDatos() {
    const conteo = {};
    this.#dataSolicitudes.tipo = [0, 0, 0]
    this.#dataSolicitudes.estado = [0, 0, 0, 0]

    for (let i = 0; i < this.#dataSolicitudes.solicitudes.length; i++) {
      const solicitud = this.#dataSolicitudes.solicitudes[i]

      const fecha = solicitud.fechaRegistro.slice(0, 10);
      conteo[fecha] = (conteo[fecha] || 0) + 1;
      
      switch (solicitud.tipoSolicitudId) {
        case 1:
          this.#dataSolicitudes.tipo[0] += 1
          break;
        case 2:
          this.#dataSolicitudes.tipo[1] += 1
          break;
        case 3:
          this.#dataSolicitudes.tipo[2] += 1
          break;
        default:
          break;
      }
      switch (solicitud.estadoSolicitudId) {
        case 1:
          this.#dataSolicitudes.estado[0] += 1
          break;
        case 2:
          this.#dataSolicitudes.estado[1] += 1
          break;
        case 3:
          this.#dataSolicitudes.estado[2] += 1
          break;
        case 4:
          this.#dataSolicitudes.estado[3] += 1
          break;
        default:
          break;
      }
    }
    this.#dataSolicitudes.conteoFechaRegistro = conteo
  }

  #generarRangoFechas(anio, mes) {
    const fechas = [];
    const fecha = new Date(anio, mes - 1, 1);
    while (fecha.getMonth() == mes - 1) {
      const iso = fecha.toISOString().slice(0, 10); // 'YYYY-MM-DD'
      fechas.push(iso);
      fecha.setDate(fecha.getDate() + 1);
    }
  
    return fechas;
  }

  #dibujarGraficos(anio, mes) {
    // gráfico de dona ()
    if (this.#graficoCanva1) {
      const labels = this.#generarRangoFechas(anio, Number.parseInt(mes));
      this.#graficoCanva1.data.labels = labels
      this.#graficoCanva1.data.datasets[0].data = labels.map(fecha => this.#dataSolicitudes.conteoFechaRegistro[fecha] || 0)
      this.#graficoCanva1.update();
      console.log(this.#graficoCanva1);
    } else {
      const labels = this.#generarRangoFechas(anio, Number.parseInt(mes));

      const ctx1 = this.htmlPanel.querySelector("canvas[id='canva1']").getContext('2d');
      const datos1 = {
        labels: labels,
        datasets: [{
          label: "Solicitudes por día",
          data: labels.map(fecha => this.#dataSolicitudes.conteoFechaRegistro[fecha] || 0),
          fill: false,
          borderColor: '#36A2EB',
          backgroundColor: 'rgba(50, 47, 235, 0.81)',
          tension: 0.4,
        }]
      }
      const config1 = {
        type: 'line',
        data: datos1,
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: {
            x: {
              title: { display: true, text: 'Fecha' }
            },
            y: {
              title: { display: true, text: 'Cantidad de solicitudes' },
              beginAtZero: true
            }
          }
        }
      }
      this.#graficoCanva1 = new Chart(ctx1, config1);
    }

    if (this.#graficoCanva2) {
      this.#graficoCanva2.data.datasets[0].data = this.#dataSolicitudes.tipo
      this.#graficoCanva2.update();
    } else {
      const ctx2 = this.htmlPanel.querySelector("canvas[id='canva2']").getContext('2d');
      const datos2 = {
        labels: ['Error', 'Capacitación', 'Requerimientos'],
        datasets: [{
          label: 'Tipo de solicitud',
          data: this.#dataSolicitudes.tipo,
          backgroundColor: ['#f87171', '#60a5fa', '#facc15'],
          borderColor: 'rgba(255,255,255,1)',
          hoverOffset: 10
        }]
      };
      const config2 = {
        type: 'doughnut',
        data: datos2,
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              position: 'bottom'
            },
            title: {
              display: true,
              text: 'Tipo de solicitud'
            }
          }
        }
      };
      this.#graficoCanva2 = new Chart(ctx2, config2);
    }

    if (this.#graficoCanva3) {
      this.#graficoCanva3.data.datasets[0].data = this.#dataSolicitudes.estado
      this.#graficoCanva3.update();
    
    } else {
      const ctx3 = this.htmlPanel.querySelector("canvas[id='canva3']").getContext('2d');
      const datos3 = {
        labels: ['Pendiente', 'En proceso', 'Asignada', 'Atendida'],
        datasets: [{
          label: 'Tipo de solicitud',
          data: this.#dataSolicitudes.estado,
          backgroundColor: ['#f87171', '#60a5fa', '#facc15', '#FFCE56'],
          hoverOffset: 10
        }]
      };
      const config3 = {
        type: 'doughnut',
        data: datos3,
        options: {
          responsive: true,
          maintainAspectRatio: false, // 👈 clave para que use el alto definido por Tailwind
          plugins: {
            legend: {
              position: 'bottom'
            },
            title: {
              display: true,
              text: 'Estado de solicitud'
            }
          }
        }
      };
      this.#graficoCanva3 = new Chart(ctx3, config3);
    }


  }
}
