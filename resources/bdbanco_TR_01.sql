DELIMITER //

DROP TRIGGER IF EXISTS after_prestamo_aprobado//

CREATE TRIGGER after_prestamo_aprobado
AFTER UPDATE ON prestamo
FOR EACH ROW
BEGIN
-- Declarar la variable i para el bucle
DECLARE i INT DEFAULT 1;
    IF NEW.estado = 'APROBADO' AND OLD.estado <> 'APROBADO' THEN
        -- Bucle para insertar las cuotas en cuotas_prestamo
        WHILE i <= NEW.plazo_pago_mes DO
            INSERT INTO cuota_prestamo (id_prestamo, importe_cuota, cuota, estado, deleted)
            VALUES (NEW.id, NEW.monto_por_mes, i, 'PENDIENTE', false);
            SET i = i + 1;
        END WHILE;

        -- Insertar un registro en la tabla de movimiento
        INSERT INTO movimiento (idcuenta_origen, idcuenta_destino, fecha, detalle, importe, tipo_movimiento)
        VALUES (NEW.id_cuenta, NEW.id_cuenta, CURDATE(), 'Alta de préstamo', NEW.importe_pedido, 'ALTA_DE_PRESTAMO');
    END IF;
END;
//

DELIMITER ;
