import tkinter as tk
import os

# Los turnos se almacenaran en un .txt
turno_file = "turno.txt"

# Función para obtener el siguiente turno
def obtener_siguiente_turno():
    # Si el archivo no existe, comenzamos en 1
    if not os.path.exists(turno_file):
        turno = 1
    else:
        with open(turno_file, "r") as f:
            turno = int(f.read()) + 1
    
    # Aqui establecemos el limite en 100 turnos 
    if turno > 100:
        turno = 1
    
    # Guardamos el nuevo turno en el archivo
    with open(turno_file, "w") as f:
        f.write(str(turno))
    
    return turno

##Interfaz Grafica

# Función que se ejecuta al presionar el botón
def generar_turno():
    turno = obtener_siguiente_turno()
    turno_label.config(text=f"Turno actual: {turno}")

# ventana principal 
ventana = tk.Tk()
ventana.title("Generador de turnos")
ventana.geometry("300x200")


label = tk.Label(ventana, text="Generador de turnos", font=("Arial", 16))
label.pack(pady=20)

#
turno_label = tk.Label(ventana, text="Turno actual: --", font=("Arial", 14))
turno_label.pack(pady=10)


boton = tk.Button(ventana, text="Generar Turno", command=generar_turno)
boton.pack(pady=10)


ventana.mainloop()
