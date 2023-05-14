```sql
CREATE TABLE `Batches` (
  `Reference` varchar(255) NOT NULL,
  `Sku` varchar(255) NOT NULL,
  `PurchasedQuantity` int NOT NULL,
  `Eta` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`Reference`)
);
```

```sql
CREATE TABLE `OrderLines` (
  `OrderId` varchar(255) NOT NULL,
  `Sku` varchar(255) NOT NULL,
  `Quantity` int NOT NULL,
  `BatchReference` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`OrderId`,`Quantity`,`Sku`),
  KEY `IX_OrderLines_Reference` (`BatchReference`),
  CONSTRAINT `FK_OrderLines_Batches_Reference` FOREIGN KEY (`BatchReference`) REFERENCES `Batches` (`Reference`) ON DELETE CASCADE
);
```