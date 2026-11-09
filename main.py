import flet as ft
import json
import os
import csv
from datetime import datetime

USUARIOS = {
    "admin": {"clave": "TruperAdmin2026", "rol": "ADMINISTRADOR"},
    "tercero": {"clave": "Truper2026", "rol": "TERCERO"},
}

DORADO = "#EFBF35"
AZUL = "#00478A"
AZULC = "#1E8FB8"
VERDE = "#008066"
CAFE = "#734D33"
ROJO = "#C70F1F"
ARCHIVO = "inventario.json"

def cargar():
    if os.path.exists(ARCHIVO):
        try:
            with open(ARCHIVO, "r", encoding="utf-8") as f:
                return json.load(f)
        except:
            return []
    return []

def guardar(datos):
    with open(ARCHIVO, "w", encoding="utf-8") as f:
        json.dump(datos, f, ensure_ascii=False, indent=2)

datos = cargar()
usuario_actual = ""
rol_actual = ""

def main(page: ft.Page):
    page.title = "Inventario Truper"
    page.bgcolor = DORADO
    page.window.width = 400
    page.window.height = 700
    page.scroll = "auto"

    def ir_login(e=None):
        page.clean()
        page.add(vista_login())

    def ir_menu(e=None):
        page.clean()
        page.add(vista_menu())

    def vista_login():
        u = ft.TextField(label="Usuario", width=320)
        c = ft.TextField(label="Contrasena", password=True, width=320)
        msg = ft.Text(" ", color=ROJO, size=16)

        def entrar(e):
            global usuario_actual, rol_actual
            user = u.value.strip()
            clave = c.value.strip()
            if user in USUARIOS and USUARIOS[user]["clave"] == clave:
                usuario_actual = user
                rol_actual = USUARIOS[user]["rol"]
                ir_menu()
            else:
                msg.value = "Usuario o clave incorrectos"
                page.update()

        return ft.Column([
            ft.Text("TRUPER", size=45, weight="bold", color="black"),
            ft.Text("INICIAR SESION", size=24, weight="bold", color="black"),
            u, c, msg,
            ft.Button("ENTRAR", width=320, bgcolor=AZUL, color="white", on_click=entrar),
            ft.Text("Admin: admin / TruperAdmin2026", size=14),
            ft.Text("Tercero: tercero / Truper2026", size=14),
        ], alignment="center", horizontal_alignment="center", spacing=15)

    def vista_menu():
        def btn(texto, color, func):
            return ft.Button(
                texto, width=160, height=70,
                bgcolor=color, color="white",
                on_click=func
            )

        fila1 = ft.Row(alignment="center")
        fila2 = ft.Row(alignment="center")
        fila3 = ft.Row(alignment="center")
        fila4 = ft.Row(alignment="center")

        if rol_actual == "ADMINISTRADOR":
            fila1.controls.append(btn("Agregar\nProducto", AZUL, lambda e: ir_agregar()))
        fila1.controls.append(btn("Ver\nTodos", AZUL, lambda e: ir_ver()))
        fila2.controls.append(btn("Buscar\npor Nombre", AZULC, lambda e: ir_buscar("nombre")))
        fila2.controls.append(btn("Buscar\npor Categoria", AZULC, lambda e: ir_buscar("categoria")))

        if rol_actual == "ADMINISTRADOR":
            fila3.controls.append(btn("Entrada\nMercancia", AZUL, lambda e: ir_mov(1)))
            fila3.controls.append(btn("Salida\nMercancia", CAFE, lambda e: ir_mov(-1)))
            fila4.controls.append(btn("Exportar\nCSV", AZULC, lambda e: ir_exportar()))
            fila4.controls.append(btn("Cerrar\nSesion", ROJO, lambda e: ir_login()))

        return ft.Column([
            ft.Text("TRUPER", size=40, weight="bold", color="black"),
            ft.Text(usuario_actual + " | " + rol_actual, size=16, color="black"),
            ft.Text("INVENTARIO TRUPER", size=24, weight="bold", color="black"),
            fila1, fila2, fila3, fila4,
        ], alignment="center", horizontal_alignment="center", spacing=12)

    def ir_agregar():
        page.clean()
        nombre = ft.TextField(label="Nombre", width=320)
        cat = ft.TextField(label="Categoria", width=320)
        medida = ft.TextField(label="Medida", width=320)
        precio = ft.TextField(label="Precio", width=320)
        cant = ft.TextField(label="Cantidad", width=320)
        msg = ft.Text("", color=VERDE, size=18)

        def guardar_prod(e):
            try:
                if not nombre.value.strip():
                    msg.value = "Escribe el nombre"
                    page.update()
                    return
                nuevo = {
                    "nombre": nombre.value.strip(),
                    "categoria": cat.value.strip(),
                    "medida": medida.value.strip(),
                    "precio": float(precio.value),
                    "cantidad": int(cant.value),
                    "fecha": datetime.now().strftime("%Y-%m-%d"),
                }
                datos.append(nuevo)
                guardar(datos)
                msg.value = "Producto guardado"
                nombre.value = ""
                cat.value = ""
                medida.value = ""
                precio.value = ""
                cant.value = ""
                page.update()
            except:
                msg.value = "Revisa precio y cantidad"
                page.update()

        page.add(ft.Column([
            ft.Text("AGREGAR PRODUCTO", size=24, weight="bold"),
            nombre, cat, medida, precio, cant, msg,
            ft.Button("GUARDAR", width=320, bgcolor=VERDE, color="white", on_click=guardar_prod),
            ft.Button("VOLVER", width=320, bgcolor=AZUL, color="white", on_click=lambda e: ir_menu()),
        ], horizontal_alignment="center", spacing=12))

    def ir_ver():
        page.clean()
        lista = []
        if not datos:
            lista.append(ft.Text("Inventario vacio", size=20))
        else:
            for i, p in enumerate(datos, 1):
                txt = str(i) + ". " + p["nombre"]
                txt += " | " + p["categoria"]
                txt += " | " + p["medida"]
                txt += " | $" + str(p["precio"])
                txt += " | " + str(p["cantidad"]) + " pzs"
                lista.append(ft.Text(txt, size=15))
        page.add(ft.Column([
            ft.Text("INVENTARIO COMPLETO", size=24, weight="bold"),
            ft.Column(lista, scroll="auto", height=400),
            ft.Button("VOLVER", width=320, bgcolor=AZUL, color="white", on_click=lambda e: ir_menu()),
        ], horizontal_alignment="center", spacing=12))

    def ir_buscar(modo):
        page.clean()
        entrada = ft.TextField(label="Escribe aqui...", width=320)
        res = ft.Text("", size=16)
        titulo = "BUSCAR POR NOMBRE"
        if modo == "categoria":
            titulo = "BUSCAR POR CATEGORIA"

        def buscar(e):
            txt = entrada.value.strip().lower()
            enc = []
            for p in datos:
                if txt in p.get(modo, "").lower():
                    enc.append(p)
            if not enc:
                res.value = "No se encontro nada"
            else:
                t = ""
                for i, p in enumerate(enc, 1):
                    t += str(i) + ". " + p["nombre"]
                    t += " | " + p["categoria"]
                    t += " | $" + str(p["precio"])
                    t += " | " + str(p["cantidad"]) + " pzs\n"
                res.value = t
            page.update()

        page.add(ft.Column([
            ft.Text(titulo, size=24, weight="bold"),
            entrada, res,
            ft.Button("BUSCAR", width=320, bgcolor=VERDE, color="white", on_click=buscar),
            ft.Button("VOLVER", width=320, bgcolor=AZUL, color="white", on_click=lambda e: ir_menu()),
        ], horizontal_alignment="center", spacing=12))

    def ir_mov(signo):
        page.clean()
        num = ft.TextField(label="Numero del producto", width=320)
        cant = ft.TextField(label="Cantidad", width=320)
        msg = ft.Text("", size=18)
        titulo = "ENTRADA MERCANCIA"
        if signo == -1:
            titulo = "SALIDA MERCANCIA"

        def aplicar(e):
            try:
                idx = int(num.value) - 1
                c = int(cant.value)
                if 0 <= idx < len(datos):
                    if signo == -1 and datos[idx]["cantidad"] < c:
                        msg.value = "No hay suficiente stock"
                        page.update()
                        return
                    datos[idx]["cantidad"] += signo * c
                    guardar(datos)
                    msg.value = "Nuevo stock: " + str(datos[idx]["cantidad"])
                else:
                    msg.value = "Numero invalido"
                page.update()
            except:
                msg.value = "Escribe numeros validos"
                page.update()

        col = CAFE
        if signo == 1:
            col = AZUL

        page.add(ft.Column([
            ft.Text(titulo, size=24, weight="bold"),
            num, cant, msg,
            ft.Button("APLICAR", width=320, bgcolor=col, color="white", on_click=aplicar),
            ft.Button("VOLVER", width=320, bgcolor=AZUL, color="white", on_click=lambda e: ir_menu()),
        ], horizontal_alignment="center", spacing=12))

    def ir_exportar():
        page.clean()
        msg = ft.Text("", color=VERDE, size=18)

        def generar(e):
            try:
                with open("inventario_exportado.csv", "w", newline="", encoding="utf-8-sig") as f:
                    w = csv.writer(f)
                    w.writerow(["Nombre", "Categoria", "Medida", "Precio", "Cantidad", "Total"])
                    for p in datos:
                        total = p["precio"] * p["cantidad"]
                        w.writerow([p["nombre"], p["categoria"], p["medida"], p["precio"], p["cantidad"], total])
                msg.value = "Archivo generado: inventario_exportado.csv"
                page.update()
            except Exception as ex:
                msg.value = "Error: " + str(ex)
                page.update()

        page.add(ft.Column([
            ft.Text("EXPORTAR A EXCEL/CSV", size=24, weight="bold"),
            msg,
            ft.Button("GENERAR ARCHIVO CSV", width=320, bgcolor=VERDE, color="white", on_click=generar),
            ft.Button("VOLVER", width=320, bgcolor=AZUL, color="white", on_click=lambda e: ir_menu()),
        ], horizontal_alignment="center", spacing=20))

    page.add(vista_login())

ft.run(main)
